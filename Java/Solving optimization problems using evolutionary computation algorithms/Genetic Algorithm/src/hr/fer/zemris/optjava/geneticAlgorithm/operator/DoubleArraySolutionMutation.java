package hr.fer.zemris.optjava.geneticAlgorithm.operator;

import java.util.Random;

import hr.fer.zemris.optjava.simulatedAnnealing.solution.DoubleArraySolution;

public class DoubleArraySolutionMutation implements IMutation<DoubleArraySolution> {

	private final Random rand;
	private final double standardDeviation;
	
	public DoubleArraySolutionMutation(Random rand, double standardDeviation) {
		this.rand = rand;
		this.standardDeviation = standardDeviation;
	}

	@Override
	public void mutate(DoubleArraySolution solution) {
		for(int i = 0; i < solution.values.length; ++i) {
			solution.values[i] += rand.nextDouble() * standardDeviation;
		}
	}

}
