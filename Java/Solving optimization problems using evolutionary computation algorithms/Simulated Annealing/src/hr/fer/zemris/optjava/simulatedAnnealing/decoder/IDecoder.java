package hr.fer.zemris.optjava.simulatedAnnealing.decoder;

import hr.fer.zemris.optjava.simulatedAnnealing.solution.SingleObjectiveSolution;


public interface IDecoder<T extends SingleObjectiveSolution> {
	double[] decode(T entity);
	void decode(T entity, double[] decodedEntity);
}
