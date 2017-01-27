package nl.utwente.iid.ilearner_tbmv.naive_multi_bayes;


import java.util.ArrayList;

public class NaiveMultinomialClassifier {

	
	// ALPHA is the parameter used for smoothing.
	double alpha = 1.0;
	
	int[] docsInClass;
	ArrayList<ArrayList<String>> docsPerClassConcat;
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
		docsPerClassConcat = new ArrayList<ArrayList<String>>();
		countDocsInClass(cats, docs);
		train();
	}
	
	
	private void train() {
		int[][][] chiTable = new int[v.getVocabulary().size()][cats.size()][2];
		for (int i = 0; i < cats.size(); i++) {
			int n_c = getDocsInClass(i);
			prior[i] = (0.0 + n_c)/n;

			//String[] txt_c = Utils.split(docsPerClassConcat[i]);
			WordCounter wc = new WordCounter(docsPerClassConcat.get(i));
			int j = 0;
			for (String word : v.getVocabulary().keySet()) {
				chiTable[j][i][0] = wc.getValue(word);
				chiTable[j][i][1] = docsPerClassConcat.get(i).size() - chiTable[j][i][0];
				//System.out.println(chiTable[j][i][0] + " "+chiTable[j][i][1]);
				//System.out.println(word);
				//System.out.println("harambe " + (wc.getValue(word)+alpha) / (wc.uniqueWords() + (wc.totalWords()*alpha)));
				//System.out.println("sletje " + wc.getValue(word));
				v.putInProb(word, i, (wc.getValue(word)+alpha) / (wc.uniqueWords() + (v.getVocabulary().size()*alpha)));
				j++;
			}
		}
		int j = 0;
		for (String word : v.getVocabulary().keySet()) {
			int[][] expect = new int[2][cats.size()];
			int w = 0;
			int wi = 0;
			
			for (int i = 0; i < cats.size(); i++) {
				w += chiTable[j][i][0];
				wi += chiTable[j][i][1];
			}
			// calculate expected values
			for (int i = 0; i < cats.size(); i++) {
				expect[0][i] = (w * (chiTable[j][i][0] + chiTable[j][i][1]))/(w+wi);
				expect[1][i] = (wi * (chiTable[j][i][1] + chiTable[j][i][1]))/(w+wi);
			}
			// calculate chi score
			double result = 0d;
			for (int i = 0; i < cats.size(); i++) {
				//(m(i,j) - e(i,j))^2 / e(i,j)
				result += Math.pow((chiTable[j][i][0] - expect[0][i]), 2d) / expect[0][i];
				result += Math.pow((chiTable[j][i][1] - expect[1][i]), 2d) / expect[1][i];
			}
			v.putInChi(word, result);
			//System.out.println(result);
			j++;
		}
	}
	
	public Category apply(Document doc) {
		double[] score = new double[cats.size()];

		String[] w = Utils.splitStripped(doc.getContents());
		for (int i = 0; i < cats.size(); i++) {
			score[i] = Math.log(prior[i]);
			int application = 0;
			for (String word : w) {
				if (v.getVocabulary().containsKey(word) && v.getChi(word) > 280 && v.getChi(word) < 300) {
					score[i] += Math.log(v.getProb(word, i));
					application++;
				}
			}
			//System.out.println(application);
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
		for (int i = 0; i < cats.size(); i++) {
			docsPerClassConcat.add(new ArrayList<>());
			for (Document doc : docs) {
				if (doc.getCategory().toString().equals(cats.get(i).toString())) {
					docsInClass[i]++;
					docsPerClassConcat.get(i).add(doc.getContents()); //TODO This probably is not the fastest way (String rebuilding on change)
				}
			}
		}
	}
	
	public int getDocsInClass(int i) {
		return docsInClass[i];
	}
}
