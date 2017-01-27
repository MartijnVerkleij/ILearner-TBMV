/**
 * Created by Ties on 27-1-2017.
 */
package nl.utwente.iid.ilearner_tbmv.naive_multi_bayes;

public class CategoryScores {
    public int getCorrect() {
        return correct;
    }

    public int getAmount() {
        return amount;
    }

    private int amount;
    private int correct;
    private Category category;

    public CategoryScores(Category category) {
        this.category = category;
        this.amount = 0;
        this.correct = 0;
    }

    public int incAmount() {
        return ++amount;
    }

    public int incCorrect() {
        return ++correct;
    }
}
