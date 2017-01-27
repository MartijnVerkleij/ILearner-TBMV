/**
 * Created by Ties on 26-1-2017.
 */
package nl.utwente.iid.ilearner_tbmv.naive_multi_bayes;

public class Utils {
    public static String[] split(String string) {
    	return splitStripped(string);
        //return string.split(" ");
    }

    public static String[] splitStripped(String string) {
    	//return split(string);
        return string.toLowerCase().split("\\W");
    }
}
