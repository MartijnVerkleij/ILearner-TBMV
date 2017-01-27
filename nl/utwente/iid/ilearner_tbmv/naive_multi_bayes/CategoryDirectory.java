/**
 * Created by Ties on 27-1-2017.
 */
package nl.utwente.iid.ilearner_tbmv.naive_multi_bayes;

import java.io.File;

public class CategoryDirectory {
    public CategoryDirectory(Category category, File directory) {
        this.category = category;
        this.directory = directory;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public File getDirectory() {
        return directory;
    }

    public void setDirectory(File directory) {
        this.directory = directory;
    }

    private Category category;
    private File directory;
}
