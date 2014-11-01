package hr.fer.zemris.optjava.simulatedAnnealing.decoder;

import hr.fer.zemris.optjava.simulatedAnnealing.solution.DoubleArraySolution;



public class PassThroughDecoder implements IDecoder<DoubleArraySolution>{
	private final double[] mins;
	private final double[] maxs;
	
	public PassThroughDecoder(double[] mins, double[] maxs) {
		this.mins = mins;
		this.maxs = maxs;
	}

	@Override
	public double[] decode(DoubleArraySolution doubleArraySolution) {
		for(int i = 0; i <  doubleArraySolution.values.length; ++i) {
			double v = doubleArraySolution.values[i];
			if(v < mins[i]) {
				v = mins[i];
			}
			
			if(v > maxs[i]) {
				v = maxs[i];
			}
			
			doubleArraySolution.values[i] = v;
		}
		
		return doubleArraySolution.values;
	}
	
	@Override
	public void decode(DoubleArraySolution doubleArraySolution, double[] decodedResult) {
		decodedResult = decode(doubleArraySolution);
	}

}