package hr.fer.zemris.optjava.geneticAlgorithm.operator;

import hr.fer.zemris.optjava.simulatedAnnealing.solution.SingleObjectiveSolution;

public interface IMutation<T extends SingleObjectiveSolution> {
	void mutate(T solution);
}
