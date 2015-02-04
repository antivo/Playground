package hr.fer.zemris.ml.lr.gd;
import java.util.Set;

import hr.fer.zemris.ml.lr.dataset.Dataset;


public class ErrorFunction implements IFunction {
	private final Dataset dataset;

	public ErrorFunction(Dataset dataset) {
		this.dataset = dataset;
	}
	
	public double getEE(double[] w) {
		int n = dataset.getN();
		int sum = 0;
		for(int i = 0; i < n; ++i) {
			int y = dataset.getLabel(i);
			double hx = Formula.sigma(w, dataset, i);
			
			if(hx >= 0.5 && y == 0) {
				++sum;
			} else if(hx < 0.5 && y == 1) {
				++sum;
			}
		}
		
		return (sum/n);
	}
	
	public double getCEE(double[] w) {
		return getError(w);
	}

	@Override
	public double[] correction(double[] deltaW, int entryIndex, double[] w) {
		double h = Formula.sigma(w, dataset, entryIndex);
		int y = dataset.getLabel(entryIndex);
		
		Set<Integer> nonZeroFeatures = dataset.getNonZeroFeaturesSet(entryIndex);
		double h_y = h-y;
		deltaW[0] += h_y;
		for(int featureIndex : nonZeroFeatures) {
			deltaW[featureIndex + 1] += h_y * dataset.getNormalizedFrequency(entryIndex, featureIndex);
		}
		
		return deltaW;
	}

	@Override
	public double getError(double[] w) {
		return Formula.crossEntropyError(w, dataset);
	}

	@Override
	public int getN() {
		return dataset.getN();
	}

}
