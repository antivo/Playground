package hr.fer.zemris.optjava.simulatedAnnealing;

import hr.fer.zemris.optjava.simulatedAnnealing.decoder.IDecoder;
import hr.fer.zemris.optjava.simulatedAnnealing.neighborhood.INeighborhood;
import hr.fer.zemris.optjava.simulatedAnnealing.solution.SingleObjectiveSolution;
import hr.fer.zemris.optjava.simulatedAnnealing.tempSchedule.ITempSchedule;

import java.util.Random;

public class SimulatedAnnealing<T extends SingleObjectiveSolution> implements IOptAlgorithm<T> {

	private IDecoder<T> decoder;
	private INeighborhood<T> neighborhood;
	private T startWith;
	private Ifunction function;
	private boolean minimize;
	private Random rand;
	private ITempSchedule tempSchedule;
	
	public SimulatedAnnealing(Random rand, IDecoder<T> decoder, INeighborhood<T> neighborhood,
			T startWith, Ifunction function, ITempSchedule tempSchedule, boolean minimize) {
		this.rand = rand;
		this.decoder = decoder;
		this.neighborhood = neighborhood;
		this.startWith = startWith;
		this.function = function;
		this.minimize = minimize;
		this.tempSchedule = tempSchedule;
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
		//int iter = 0;
		do {
			double temp = tempSchedule.getNextTemperature();
			//System.out.println("TEMP: " + temp + " iter: " + iter);
			
			T randomNeighbour = neighborhood.randomNeighbor(solution);
			evaluateSolution(randomNeighbour);
			
			System.out.println("Neighbor: " + randomNeighbour);
			
			double delta = solution.fitness - randomNeighbour.fitness;
			
			if(delta <= 0) {
				//System.out.println("DELTA " + delta);
				solution = randomNeighbour;
			} else {
				double p = Math.exp(-delta / temp);
				double decisionPoint = rand.nextDouble();
				//System.out.println("EVO " + p + " / " + decisionPoint );
				if(p > decisionPoint) {
					solution = randomNeighbour;
				}
			}
			System.out.println("Solution: " + solution);
			//++iter;
			
		} while(!tempSchedule.isFrozen());
		
		
		return solution;
	}

}
