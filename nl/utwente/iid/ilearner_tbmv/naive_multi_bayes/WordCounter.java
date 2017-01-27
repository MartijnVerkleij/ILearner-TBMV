package nl.utwente.iid.ilearner_tbmv.naive_multi_bayes;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class WordCounter{
	
	TreeMap<String, Integer> wordcounter;
	int totalWords = 0;
	
	public WordCounter(ArrayList<String> docs) {
		wordcounter = new TreeMap<>();
		String docString = "";
		for (String doc : docs) {
			docString += doc;
			
		}

		String[] docs_split = Utils.splitStripped(docString);
		for (totalWords = 0; totalWords < docs_split.length; totalWords++) {
			if (wordcounter.containsKey(docs_split[totalWords])) {
				wordcounter.put(docs_split[totalWords], wordcounter.get(docs_split[totalWords])+1);
			} else {
				wordcounter.put(docs_split[totalWords], 1);
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
	
	public int totalWords() {
		return totalWords - 1;
	}
	

}
