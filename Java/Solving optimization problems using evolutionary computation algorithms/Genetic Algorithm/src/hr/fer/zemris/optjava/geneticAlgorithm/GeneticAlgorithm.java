package hr.fer.zemris.optjava.geneticAlgorithm;

import java.util.Random;

import hr.fer.zemris.optjava.geneticAlgorithm.operator.ICrossover;
import hr.fer.zemris.optjava.geneticAlgorithm.operator.IEvaluator;
import hr.fer.zemris.optjava.geneticAlgorithm.operator.IMutation;
import hr.fer.zemris.optjava.geneticAlgorithm.population.IPopulation;
import hr.fer.zemris.optjava.geneticAlgorithm.selection.ISelection;
import hr.fer.zemris.optjava.simulatedAnnealing.solution.DoubleArraySolution;
import hr.fer.zemris.optjava.simulatedAnnealing.solution.SingleObjectiveSolution;

public class GeneticAlgorithm<T extends SingleObjectiveSolution> {
	/*private static void assertMinError(double minError) {
		if(minError < 0) {
			throw new RuntimeException("minimal distance must be positive");
		}
	}*/
	
	public T run(Random rand, IPopulation<T> population, IEvaluator<T> evaluator, ISelection<T> selection, ICrossover<T> crossover, IMutation<T> mutation, int maxIterations , double minError, boolean elitism) {
		int iteration = 0;
		T bestSolution = population.getBest();
		while(maxIterations > iteration++ && Math.abs(bestSolution.fitness) > minError)  {
			population = selection.makePopulation(population, evaluator, crossover, mutation, elitism);
			bestSolution = population.getBest();
			
			for(T das : population) {
				System.out.println(das);
			}
			
			System.out.println(bestSolution);
			System.out.println();
			
		}
		
		return bestSolution;
	}
}
