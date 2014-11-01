package hr.fer.zemris.optjava.simulatedAnnealing.neighborhood;

import hr.fer.zemris.optjava.simulatedAnnealing.solution.SingleObjectiveSolution;



public interface INeighborhood<T extends SingleObjectiveSolution> {
	T randomNeighbor(T current);
}
