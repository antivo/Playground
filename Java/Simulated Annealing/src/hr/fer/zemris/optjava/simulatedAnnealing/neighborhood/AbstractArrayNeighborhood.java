package hr.fer.zemris.optjava.simulatedAnnealing.neighborhood;

import hr.fer.zemris.optjava.simulatedAnnealing.solution.DoubleArraySolution;

public abstract class AbstractArrayNeighborhood implements INeighborhood<DoubleArraySolution> {
	private final double[] deltas;
	
	private static void assertDimensions(double[] deltas, double[] current) {
		if(deltas.length != current.length) {
			throw new RuntimeException("Neighborhood not defined for given solution. Neighborhood works on dimensions of " + deltas.length + " but was given " + current.length);
		}
	}
	
	public AbstractArrayNeighborhood(double[] deltas) {
		this.deltas = deltas;
	}
	
	@Override
	public DoubleArraySolution randomNeighbor(DoubleArraySolution current) {
		assertDimensions(deltas, current.values);
		DoubleArraySolution solution = current.duplicate();
		int position = (int) Math.floor(getFromUnitInterval() * current.values.length);

		double randInInterval = (getFromUnitInterval() - 0.5) * 2;
		solution.values[position] += randInInterval * deltas[position];

		return solution;
	}
	
	protected abstract double getFromUnitInterval();
}
