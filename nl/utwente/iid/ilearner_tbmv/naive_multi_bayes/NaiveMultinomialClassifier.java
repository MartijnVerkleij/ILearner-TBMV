package nl.utwente.iid.ilearner_tbmv.naive_multi_bayes;


import java.util.ArrayList;

public class NaiveMultinomialClassifier {

	
	// ALPHA is the parameter used for smoothing.
	double alpha = 1.0;
	
	int[] docsInClass;
	String[] docsPerClassConcat;
	ArrayList<Category> cats;
	ArrayList<Document> docs;
	private double[] prior;
	private int n;
	private Vocabulary v;
	
	public NaiveMultinomialClassifier(ArrayList<Category> cats, ArrayList<Document> docs, double alpha) {
		this.alpha = alpha;
		this.cats = cats;
		this.docs = docs;
		v = new Vocabulary(docs);
		n = docs.size();
		prior = new double[cats.size()];
		docsInClass = new int[cats.size()];
		docsPerClassConcat = new String[cats.size()];
		countDocsInClass(cats, docs);
		train();
	}
	
	
	private void train() {
		for (int i = 0; i < cats.size(); i++) {
			int n_c = getDocsInClass(i);
			prior[i] = (0.0 + n_c)/n;

			String[] txt_c = Utils.split(docsPerClassConcat[i]);
			WordCounter wc = new WordCounter(docsPerClassConcat[i]);
			for (String word : v.getVocabulary().keySet()) {
				//System.out.println("harambe " + (wc.getValue(word)+alpha) / (wc.uniqueWords() + (wc.totalWords()*alpha)));
				//System.out.println("sletje " + wc.getValue(word));
				v.putInProb(word, i, (wc.getValue(word)+alpha) / (wc.uniqueWords() + (wc.totalWords()*alpha)));
			}
		}
	}
	
	public Category apply(Document doc) {
		double[] score = new double[cats.size()];

		String[] w = Utils.split(doc.getContents());
		for (int i = 0; i < cats.size(); i++) {
			score[i] = Math.log(prior[i]);

			for (String word : w) {
				if (v.getVocabulary().containsKey(word)) {
					score[i] += Math.log(v.getProb(word, i));
				}
			}
		}
		int highestScorer = -1;
		double highScore = -Double.MAX_VALUE;
		for (int i = 0; i < score.length; i++) {
			if (score[i] > highScore) {
				highScore = score[i];
				highestScorer = i;
			}
		}

		return cats.get(highestScorer);
	}
	
	
	/**
	 * Counts the number of documents that have the same classifier as <cat>.
	 * Algorithm is O(<docs>).
	 * @param cats Category that count is requested from
	 * @param docs Document set that are classified
	 * @return total of documents classified as <cat>
	 */
	// May want to incorporate ConcatenateAllTextsOfDocsInClass(D, c)
	// and CountTokensOfTerm(T xt c , t) in a smart way...
	private void countDocsInClass(ArrayList<Category> cats, ArrayList<Document> docs) {
		for (Document doc : docs) {
			for (int i = 0; i < cats.size(); i++) {
				if (doc.getCategory().toString().equals(cats.get(i).toString())) {
					docsInClass[i]++;
					docsPerClassConcat[i] += " " + doc.getContents(); //TODO This probably is not the fastest way (String rebuilding on change)
				}
			}
		}
	}
	
	public int getDocsInClass(int i) {
		return docsInClass[i];
	}
}
