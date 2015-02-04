package hr.fer.zemris.ml.lr.dataset.entry;

import java.util.Map;
import java.util.Set;

public class Entry {
	private static void assertLabel(int label) {
		if(label != 0 && label != 1) {
			throw new IllegalArgumentException("Entry can only be labeled as belonging or not belonging to class");
		}
	}
	
	private final int label;
	private final Map<Integer, Double> features;
	public Entry(int label, Map<Integer, Double> features) {
		assertLabel(label);
		
		this.label = label;
		this.features = features;
	}
	
	public int getLabel() {
		return label;
	}
	
	public Set<Integer> getNonZeroFeaturesSet() {
		return features.keySet();
	}
	
	public double getScaledFrequency(int wordIndex) {
		if(features.containsKey(wordIndex)) { 
			return features.get(wordIndex);
		}
		return 0;
	}

}
