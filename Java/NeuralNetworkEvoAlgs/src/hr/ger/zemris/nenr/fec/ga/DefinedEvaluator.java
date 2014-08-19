package hr.ger.zemris.nenr.fec.ga;

import hr.fer.zemris.nenr.fec.ds.Dataset;
import hr.fer.zemris.nenr.fec.nn.NeuralNetwork;

public class DefinedEvaluator {
	private final NeuralNetwork nn;
	private final Dataset ds;
	
	public DefinedEvaluator(NeuralNetwork nn, Dataset ds) {
		super();
		this.nn = nn;
		this.ds = ds;
	}
	
	public double calcError(double[] weights) {
		return nn.calcError(ds, weights);
	}
}
