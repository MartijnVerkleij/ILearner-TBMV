package nl.utwente.iid.ilearner_tbmv.naive_multi_bayes;

public class NaiveMultinomialClassifier {

	
	
	public void train(Category[] cats, Document[] docs) {
		Vocabulary v = new Vocabulary(docs);
		int n = docs.length;
		int[] prior = new int[cats.length];
		for (int i = 0; i < cats.length; i++) {
			int n_c = countDocsInClass(cats[i], docs);
			prior[i] = n_c;
			
			/*	Txt c = ConcatenateAllTextsOfDocsInClass(D, c);
				foreach t in V do
					Tct = CountTokensOfTerm(T xt c , t);
					foreach t in V do
						condprob[t][c] = bladiebla_zie_slides ;
				
					end
				end
			 */
			
		}
	}
	
	public Category apply(Category[] cats, Vocabulary v, int[] prior, int[][] condprob/*, d*/) {
		return null;
	}
	
	
	/**
	 * Counts the number of documents that have the same classifier as <cat>.
	 * Algorithm is O(<docs>).
	 * @param cat Category that count is requested from
	 * @param docs Document set that are classified
	 * @return total of documents classified as <cat>
	 */
	// May want to incorporate ConcatenateAllTextsOfDocsInClass(D, c)
	// and CountTokensOfTerm(T xt c , t) in a smart way...
	private int countDocsInClass(Category cat, Document[] docs) {
		int count = 0; 
		for (Document d : docs) {
			if (d.category.equals(cat.toString())) {
				count++;
			}
		}
		return count;
	}
}
