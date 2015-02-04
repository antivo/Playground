package hr.fer.zemris.ml.lr.gd;
import java.util.Set;

import hr.fer.zemris.ml.lr.dataset.Dataset;


public class Formula {
	private static double alpha(double[] w, Dataset dataset, int entryIndex) {
		double result = w[0];
		Set<Integer> nonZeroFeatures = dataset.getNonZeroFeaturesSet(entryIndex);
		for(int featureIndex : nonZeroFeatures) {
			result = w[featureIndex + 1] * dataset.getNormalizedFrequency(entryIndex, featureIndex);
		}
		
		return result;
	}
	
	private static double h(double[] w, Dataset dataset, int entryIndex) {
		double a = alpha(w, dataset, entryIndex);
		double n = 1 + Math.exp(-a);
		
		return (1/n);
	}
	
	public static double sigma(double[] w, Dataset dataset, int entryIndex) {
		return h(w, dataset, entryIndex);
	}
	
	public static double crossEntropyError(double[] w, Dataset dataset) {
		double sum = 0;
		double N = dataset.getN();
		for(int i = 0; i < N; ++i) {
			int y = dataset.getLabel(i);
			double hx = h(w, dataset, i);
			
			sum += y * Math.log(hx) + (1 - y) * Math.log(1 - hx);
		}
		
		double result = -sum;
		return result;
	}
}
