/**
 * Created by Ties on 26-1-2017.
 */
package nl.utwente.iid.ilearner_tbmv.naive_multi_bayes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Scanner;

public class DocumentReader extends Thread{
    public static final boolean DBG = true;
    ArrayList<Document> documents;

    @Override
    public void run() {
        documents = new ArrayList<>();

        System.out.println("Current directory: " + System.getProperty("user.dir"));

        ArrayList<Category> categories = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);

        if (!DBG) {
            boolean scanning = true;
            while (scanning) {
                System.out.println("Name of category (or \"train\" to start):");

                Category cat = new Category(scanner.nextLine());
                if (cat.toString().equals("train")) {
                    scanning = false;
                    continue;
                }
                categories.add(cat);

                System.out.println("Directory containing training data for category \"" + cat + "\":");
                String directory = scanner.nextLine();
                File directoryFile = new File(directory);
                scanFile(directoryFile, cat, documents);
                System.out.println(documents.size());
            }
        } else {
            categories.add(new Category("m"));
            categories.add(new Category("f"));

            File directoryFile = new File("../blogs/M/train");
            System.out.println(categories.get(0));
            scanFile(directoryFile, categories.get(0), documents);

            directoryFile = new File("../blogs/F/train");
            scanFile(directoryFile, categories.get(1), documents);
        }

        NaiveMultinomialClassifier nmc = new NaiveMultinomialClassifier(categories, documents, 1.0);

        int[] scores = new int[categories.size()];
        int total = 0;

        for (int i = 0; i < categories.size(); i++) {
            System.out.println("folder for test data cat " + categories.get(i).toString());

            ArrayList<Document> testDocs = new ArrayList<>();
            String directory = scanner.nextLine();
            File directoryFile = new File(directory);
            scanFile(directoryFile, null, testDocs);
            for (Document doc : testDocs) {
                Category cat = nmc.apply(doc);
                scores[categories.indexOf(cat)] += 1;
                total++;
            }
            System.out.println("Total: " + total);
            for (int j = 0; j < scores.length; j++) {
                System.out.println(categories.get(i).toString() + ": " + scores[i]);
            }
        }
    }

    private void scanFile(File file, Category cat, ArrayList<Document> documents) {
        if (file.isDirectory()) {
            for (File subFile : file.listFiles()) {
                scanFile(subFile, cat, documents);
            }
        } else if (file.isFile()) {
            try {
                Document document = new Document(cat, getFileContents(file));
                documents.add(document);
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    private static String getFileContents(File file) throws IOException {
        if (!file.isFile())
            return null;
        return new String(Files.readAllBytes(file.toPath()));
    }

    public static void main(String[] args) {
        new DocumentReader().start();
    }
}
