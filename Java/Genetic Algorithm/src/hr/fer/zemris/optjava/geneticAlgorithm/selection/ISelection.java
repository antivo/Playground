package hr.fer.zemris.optjava.geneticAlgorithm.selection;

import hr.fer.zemris.optjava.geneticAlgorithm.operator.ICrossover;
import hr.fer.zemris.optjava.geneticAlgorithm.operator.IEvaluator;
import hr.fer.zemris.optjava.geneticAlgorithm.operator.IMutation;
import hr.fer.zemris.optjava.geneticAlgorithm.population.IPopulation;
import hr.fer.zemris.optjava.simulatedAnnealing.solution.SingleObjectiveSolution;

public interface ISelection<T extends SingleObjectiveSolution> {
	T makeChromosome(IPopulation<T> population, IEvaluator<T> evaluator, ICrossover<T> crossover, IMutation<T> mutation);
	IPopulation<T> makePopulation(IPopulation<T> population, IEvaluator<T> evaluator, ICrossover<T> crossover, IMutation<T> mutation, boolean elitism);
}