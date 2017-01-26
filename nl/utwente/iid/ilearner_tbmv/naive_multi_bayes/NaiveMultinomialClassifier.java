package nl.utwente.iid.ilearner_tbmv.naive_multi_bayes;




public class NaiveMultinomialClassifier {

	
	// ALPHA is the parameter used for smoothing.
	double alpha = 1.0;
	
	int[] docsInClass;
	String[] docsPerClassConcat;
	Category[] cats;
	Document[] docs;
	private double[][] condprob;
	private int[] prior;
	private int n;
	private Vocabulary v;
	
	public NaiveMultinomialClassifier(Category[] cats, Document[] docs, double alpha) {
		this.alpha = alpha;
		this.cats = cats;
		this.docs = docs;
		v = new Vocabulary(docs);
		n = docs.length;
		prior = new int[cats.length];
		countDocsInClass(cats, docs);
		condprob = new double[v.size()][cats.length];
		train();
	}
	
	
	private void train() {
		for (int i = 0; i < cats.length; i++) {
			int n_c = getDocsInClass(i);
			prior[i] = n_c;
			
			String[] txt_c = Utils.split(docsPerClassConcat[i]);
			WordCounter wc = new WordCounter(txt_c[i]);
			
			for (int j = 0; j < v.size(); j++) {
				condprob [j][i] = ((wc.getWordCounter().get(j)+alpha) / (wc.totalWords() + (wc.uniqueWords()*alpha)));
			}
		}
	}
	
	public Category apply(Category[] cats, Vocabulary v, int[] prior, int[][] condprob) {
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
	private void countDocsInClass(Category[] cats, Document[] docs) {
		for (Document doc : docs) {
			for (int i = 0; i < cats.length; i++) {
				if (doc.getCategory().equals(cats[i].toString())) {
				docsInClass[i]++;
				docsPerClassConcat[i] += " " + doc.getContents();
				}
			}
		}
	}
	
	public int getDocsInClass(int i) {
		return docsInClass[i];
	}
}
