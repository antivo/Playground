package hr.fer.zemris.optjava.geneticAlgorithm.operator;

import hr.fer.zemris.optjava.simulatedAnnealing.solution.SingleObjectiveSolution;

public interface IEvaluator<T extends SingleObjectiveSolution> {
	void evaluate(T solution);
}
