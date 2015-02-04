package hr.fer.zemris.optjava.geneticAlgorithm.operator;

import java.util.Random;

import hr.fer.zemris.optjava.simulatedAnnealing.solution.DoubleArraySolution;

public class DoubleArraySolutionCrossover implements ICrossover<DoubleArraySolution> {
	private final double alpha;
	private final Random rand;
	
	public DoubleArraySolutionCrossover(Random rand, double alpha) {
		this.rand = rand;
		this.alpha = alpha;
	}
	
	@Override
	public DoubleArraySolution makeChild(DoubleArraySolution fstParent,
			DoubleArraySolution sndParent) {
		DoubleArraySolution child = fstParent.newLikeThis();
		for(int i = 0; i < child.values.length; ++i) {
			double fst = fstParent.values[i];
			double snd = sndParent.values[i];
			double min = Math.min(fst, snd);
			double max = Math.max(fst, snd);
			double I = max - min;
			
			double lo = min - I * alpha;
			double hi = max + I * alpha;
			
			double l = hi - lo;
					
			child.values[i] = rand.nextDouble() * l + lo;
		}
		
		return child;
	}

}
