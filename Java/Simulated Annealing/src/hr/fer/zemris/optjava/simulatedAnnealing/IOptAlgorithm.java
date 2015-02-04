package hr.fer.zemris.optjava.simulatedAnnealing;

import hr.fer.zemris.optjava.simulatedAnnealing.solution.SingleObjectiveSolution;



public interface IOptAlgorithm<T extends SingleObjectiveSolution> {
	T run();
}
