/**
 * Created by Ties on 26-1-2017.
 */
package nl.utwente.iid.ilearner_tbmv.naive_multi_bayes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

public class Utils {
    public static String[] split(String string) {
    	return splitStripped(string);
        //return string.split(" ");
    }

    public static String[] splitStripped(String string) {
    	//return split(string);
        return string.toLowerCase().split("\\W");
    }

    public static String getFileContents(File file) throws IOException {
        if (!file.isFile())
            return null;
        return new String(Files.readAllBytes(file.toPath()));
    }

    public static void scanFile(File file, Category cat, ArrayList<Document> documents) {
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
}
