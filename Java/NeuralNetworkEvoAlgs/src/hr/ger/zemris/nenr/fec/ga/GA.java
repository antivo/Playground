package hr.ger.zemris.nenr.fec.ga;

import hr.ger.zemris.nenr.fec.ga.chromosome.Chromosome;

import java.util.Random;

public class GA {
	// random
	private final Random rand;
	
	// iterations
	private final int iterations;
	
	// chromosome
	private final int chromosomeLength;
	
	// population
	private final int populationSize;
	
	// mutation
	private final double v1;
	private final double pcAdd;
	private final double sigmaAdd;
	private final double pcReplace;
	private final double sigmaReplace;
	
	// mse
	private final double mseTarget = 1e-7;

	public GA(Random rand, int iterations, int chromosomeLength, int populationSize,
			double v1, double pcAdd, double sigmaAdd, double pcReplace,
			double sigmaReplace) {
		this.rand = rand;
		this.iterations = iterations;
		this.chromosomeLength = chromosomeLength;
		this.populationSize = populationSize;
		this.v1 = v1;
		this.pcAdd = pcAdd;
		this.sigmaAdd = sigmaAdd;
		this.pcReplace = pcReplace;
		this.sigmaReplace = sigmaReplace;
	}



	public Chromosome train(DefinedEvaluator evaluator) {
		int numberOfIterations = iterations / populationSize;
		int iteration = 0;
		
		double bestMSE = 1 + mseTarget;
		Population population = new Population(rand, populationSize, chromosomeLength, evaluator, v1, pcAdd, pcReplace, sigmaAdd, sigmaReplace);
		System.out.println("Initial Population Error: "+ String.format("%.4f", population.getErrorSum()));
		do {
			
			Chromosome best = population.nextGeneration(rand, evaluator);
			double populationError = population.getErrorSum();
			System.out.println("Iteration " + iteration + ". Population Error: "+ String.format("%.4f", populationError));
			
			bestMSE = best.getError();
			iteration++;
		} while(iteration < numberOfIterations || bestMSE < mseTarget);
		return population.getMinimum();
	}

}
