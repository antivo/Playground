package hr.fer.zemris.optjava.simulatedAnnealing;

import hr.fer.zemris.optjava.simulatedAnnealing.decoder.IDecoder;
import hr.fer.zemris.optjava.simulatedAnnealing.neighborhood.INeighborhood;
import hr.fer.zemris.optjava.simulatedAnnealing.solution.SingleObjectiveSolution;


public class GreedyAlgorithm<T extends SingleObjectiveSolution> implements IOptAlgorithm<T> {

	private final double epsilon = 1e-12;
	
	private IDecoder<T> decoder;
	private INeighborhood<T> neighborhood;
	private T startWith;
	private Ifunction function;
	private int maxIterations;
	private boolean minimize;
	
	public GreedyAlgorithm(IDecoder<T> decoder, INeighborhood<T> neighborhood,
			T startWith, Ifunction function, int maxIterations, boolean minimize) {
		this.decoder = decoder;
		this.neighborhood = neighborhood;
		this.startWith = startWith;
		this.function = function;
		this.maxIterations = maxIterations;
		this.minimize = minimize;
	}
	
	private void evaluateSolution(T solution) {
		double[] point = decoder.decode(solution);
		solution.value = function.valeAt(point);
		solution.fitness = solution.value;
		if(minimize) {
			solution.fitness = -solution.fitness;
		}
	}
	
	@Override
	public T run() {
		T solution = (T) startWith.duplicate();
		evaluateSolution(solution);
		
		System.out.println("Solution: " + solution);
		int iter = 0;
		while(maxIterations > iter){
			T randomNeighbour = neighborhood.randomNeighbor(solution);
			evaluateSolution(randomNeighbour);
			
			System.out.println("Neighbor: " + randomNeighbour);
			if(randomNeighbour.fitness > solution.fitness) {
				solution = randomNeighbour;
			}
			
			++iter;
			System.out.println(iter + ": Solution: " + solution);
		}
		return solution;
	}	
	
	
}
