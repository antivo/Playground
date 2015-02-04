package hr.fer.zemris.ml.data;

import java.util.List;
import java.util.Map;

import Jama.Matrix;

public class Data {
	private final Map<String, List<Matrix>> classEntries;
	private final List<Matrix> orderedExamples;
	private final int N;
	
	
	
	public List<Matrix> getOrderedExamples() {
		return orderedExamples;
	}

	public Data(Map<String, List<Matrix>> data, int numberOfExamples, List<Matrix> orderedExamples) {
		this.classEntries = data;
		this.N = numberOfExamples;
		this.orderedExamples = orderedExamples;
	}

	public Map<String, List<Matrix>> getClassEntries() {
		return classEntries;
	}

	public int getN() {
		return N;
	}
	
}
