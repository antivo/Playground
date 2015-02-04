package hr.fer.zemris.optjava.geneticAlgorithm.operator;

import hr.fer.zemris.optjava.simulatedAnnealing.solution.SingleObjectiveSolution;

public interface ICrossover<T extends SingleObjectiveSolution> {
	T makeChild(T fstParent, T sndParent);
}
