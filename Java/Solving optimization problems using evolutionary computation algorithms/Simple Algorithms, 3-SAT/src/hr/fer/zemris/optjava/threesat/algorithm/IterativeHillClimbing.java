package hr.fer.zemris.optjava.threesat.algorithm;

import java.util.Random;

import hr.fer.zemris.optjava.threesat.BitVector;
import hr.fer.zemris.optjava.threesat.BitVectorNGenerator;
import hr.fer.zemris.optjava.threesat.SATFormula;

public class IterativeHillClimbing implements Algorithm {

	private static final int maxIterations = 100000;
	
	private Random rand;
	
	public IterativeHillClimbing(Random random) {
		this.rand = random;
	}
	
	@Override
	public BitVector compute(SATFormula satFormula) {
		BitVector oneSolution = null;
		int numberOfVariables = satFormula.getNumberOfVariables();
		if(0 == numberOfVariables) return null;
		BitVector test = new BitVector(rand, numberOfVariables);
		int testFit = satFormula.getNumberOfSatisfiedClauses(test);
		
		int iterations = maxIterations;
		while(iterations-- > 0) {			// until max iterations
			if(satFormula.isSatisfied(test)) {
				System.out.println(test);
				break;
			}
			BitVectorNGenerator generator = new BitVectorNGenerator(test); // base neighborhood on test
			BitVector[] neighborhood = generator.craeteNeighborhood(); // create neighborhood
			BitVector bestNeighbour = neighborhood[0];
			int max = satFormula.getNumberOfSatisfiedClauses(bestNeighbour);
			for(int i = 1; i < neighborhood.length; ++i) { // choose best neighbor
				BitVector trial = neighborhood[i];
				int fitTrial = satFormula.getNumberOfSatisfiedClauses(trial);
				if(fitTrial > max) {
					bestNeighbour = trial;
					max = fitTrial;
				}
			}
			if(testFit >= max) {
				break;
			} else {
				test = bestNeighbour;
				testFit = max;
			}			
		}
		if(satFormula.isSatisfied(test)) {
			oneSolution = test;
		}
		return oneSolution;
	}

}
