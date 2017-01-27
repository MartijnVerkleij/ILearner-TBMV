package nl.utwente.iid.ilearner_tbmv.naive_multi_bayes;

import java.util.ArrayList;
import java.util.HashMap;

public class Vocabulary {
	
	private HashMap<String, ArrayList<Double>> vocabulary;
	
	public Vocabulary(ArrayList<Document> docs) {
		if (vocabulary == null)
			vocabulary = new HashMap();
		for (Document d : docs) {
			for (String w : d.getStrippedContentsAsArray()) {
				vocabulary.put(w, new ArrayList<>());
			}
		}
	}

	public double getProb(String word, int cat) {
		if (vocabulary.get(word) != null) {
			return vocabulary.get(word).get(cat);
		} else
			return 0;
	}

	public void putInProb(String word, int cat, double value) {
		vocabulary.get(word).add(cat, value);
	}
	
	public HashMap<String, ArrayList<Double>> getVocabulary() {
		return vocabulary;
	}
	public void putInChi(String word, double value) {
		vocabulary.get(word).add(value);
	}
	public void getChi(String word) {
		vocabulary.get(word).get(vocabulary.get(word).size() - 1);
	}
	
	public int size() {
		return vocabulary.size();
	}
}
