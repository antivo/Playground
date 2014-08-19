package hr.fer.zemris.ml.lr.dataset;

import hr.fer.zemris.ml.lr.dataset.entry.Entry;

import java.util.List;
import java.util.Set;

public class Dataset {
	private final List<Entry> entries;

	public Dataset(List<Entry> entries) {
		this.entries = entries;
	}
	
	public int getN() {
		return entries.size();
	}
	
	public double getNormalizedFrequency(int entryIndex, int featureIndex) {
		return entries.get(entryIndex).getScaledFrequency(featureIndex);
	}
	
	public int getLabel(int entryIndex) {
		return entries.get(entryIndex).getLabel();
	}
	
	public Set<Integer> getNonZeroFeaturesSet(int entryIndex) {
		return entries.get(entryIndex).getNonZeroFeaturesSet();
	}
}
