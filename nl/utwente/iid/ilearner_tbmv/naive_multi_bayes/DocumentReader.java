/**
 * Created by Ties on 26-1-2017.
 */
package nl.utwente.iid.ilearner_tbmv.naive_multi_bayes;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
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
                Utils.scanFile(directoryFile, cat, documents);
                System.out.println(documents.size());
            }
        } else {
            categories.add(new Category("m"));
            categories.add(new Category("f"));

            File directoryFile = new File("../blogs/M/train");
            System.out.println(categories.get(0));
            Utils.scanFile(directoryFile, categories.get(0), documents);

            directoryFile = new File("../blogs/F/train");
            Utils.scanFile(directoryFile, categories.get(1), documents);
        }

        NaiveMultinomialClassifier nmc = new NaiveMultinomialClassifier(categories, documents, 1.0);

        HashMap<Category, ArrayList<Document>> testDocsByCat = new HashMap<>();
        int score[] = new int[categories.size()];
        int amount[] = new int[categories.size()];

        for (Category category : categories) {
            testDocsByCat.put(category, new ArrayList<>());

            System.out.println("Folder for test data category " + category.toString() + ":");
            String dir = scanner.nextLine();
            File directoryFile = new File(dir);
            Utils.scanFile(directoryFile, category, testDocsByCat.get(category));
        }

        for (Category category : categories) {
            int i = categories.indexOf(category);

            for (Document document : testDocsByCat.get(category)) {
                amount[i]++;
                Category result = nmc.apply(document);
                if (result.equals(category)) {
                    score[i]++;
                }
            }

            System.out.println(category + ": " + score[i] + " out of " + amount[i]);
        }
    }

    public static void main(String[] args) {
        new DocumentReader().start();
    }
}
