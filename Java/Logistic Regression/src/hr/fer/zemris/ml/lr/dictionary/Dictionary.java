package hr.fer.zemris.ml.lr.dictionary;

import java.util.List;

public class Dictionary {
	private final List<String> words;
	private final List<Integer> frequencies;
	
	private static void assertDictionary(List<String> words, List<Integer> frequency) {
		if(words.size() != frequency.size()) {
			throw new IllegalArgumentException("For every word must exist frequency and for every frequency must exist a word");
		}
	}
	
	public Dictionary(List<String> words, List<Integer> frequencies) {
		assertDictionary(words, frequencies);
		this.words = words;
		this.frequencies = frequencies;
	}
	
	public int getNumOfWords() {
		return frequencies.size();
	}
	
	public int getFrequency(int indexOfWord) {
		return frequencies.get(indexOfWord);
	}
	
	public String getWord(int indexOfWord) {
		return words.get(indexOfWord);
	}
}
