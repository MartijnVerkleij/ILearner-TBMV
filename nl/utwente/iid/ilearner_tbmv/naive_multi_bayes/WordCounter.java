package nl.utwente.iid.ilearner_tbmv.naive_multi_bayes;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class WordCounter{
	
	TreeMap<String, Integer> wordcounter;
	
	public WordCounter(ArrayList<String> docs) {
		wordcounter = new TreeMap<>();
		for (String doc : docs) {
			Vocabulary v = new Vocabulary(Utils.splitStripped(doc));
			for (String word : v.getVocabulary().keySet()) {
				if (wordcounter.containsKey(word)) {
					wordcounter.put(word, wordcounter.get(word)+1);
				} else {
					wordcounter.put(word, 1);
				}
			}
		}
	}

	public int getValue(String word) {
		if (wordcounter.get(word) != null) {
			return wordcounter.get(word);
		} else {
			return 0;
		}
	}

	public Map<String, Integer> getWordCounter() {
		return wordcounter;
	}
	
	public int uniqueWords() {
		return wordcounter.size();
	}
	

}
