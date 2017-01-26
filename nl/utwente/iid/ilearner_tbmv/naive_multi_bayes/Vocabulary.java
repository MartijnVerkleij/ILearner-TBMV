package nl.utwente.iid.ilearner_tbmv.naive_multi_bayes;

import java.util.TreeSet;

public class Vocabulary {
	
	TreeSet<String> vocabulary;
	
	public Vocabulary(Document[] docs) {
		for (Document d : docs) {
			for (String w : d.getContentsAsArray()) {
				vocabulary.add(w);
			}
		}
	}
	
	public TreeSet<String> getVocabulary() {
		return vocabulary;
	}
	
	public int size() {
		return vocabulary.size();
	}
}
