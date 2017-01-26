package nl.utwente.iid.ilearner_tbmv.naive_multi_bayes;

import java.util.Map;
import java.util.TreeMap;

public class WordCounter{
	
	TreeMap<String, Integer> wordcounter = new TreeMap<>();
	int totalWords = 0;
	
	
	public WordCounter(String docs) {
		String[] docs_split = Utils.split(docs); 
		for (totalWords = 0; totalWords < docs_split.length; totalWords++) {
			if (wordcounter.containsKey(docs_split[totalWords])) {
				wordcounter.put(docs_split[totalWords], wordcounter.get(docs_split[totalWords])+1);
			} else {
				wordcounter.put(docs_split[totalWords], 1);
			}
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
