package hr.fer.zemris.optjava.geneticAlgorithm.selection;

import hr.fer.zemris.optjava.geneticAlgorithm.population.IPopulation;
import hr.fer.zemris.optjava.simulatedAnnealing.solution.SingleObjectiveSolution;

public abstract class AbstractSelection<T extends SingleObjectiveSolution> implements ISelection<T> {
	private void assertTargetPopulation(IPopulation<T> target) {
		if(target.getSize() < 1) {
			throw new RuntimeException("new population is empty");
		}
	}
	
	void elitism(IPopulation<T> original, IPopulation<T> target) {
		assertTargetPopulation(target);
		target.setChromosome(original.getBest(), 0);
	}
}
