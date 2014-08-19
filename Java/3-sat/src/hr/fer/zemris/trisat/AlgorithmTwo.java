package hr.fer.zemris.trisat;

import java.util.Random;

public class AlgorithmTwo extends Algorithm {
	private Random rand;
	private int numberOfIterations;
	
	public AlgorithmTwo(SATFormula satFormula, Random rand, int numberOfITerations)  throws IllegalStateException  {
		super(satFormula);
		if(null == rand) {
			throw new IllegalStateException("Random Number Generator Needed for Algorithm Two");
		}
		this.rand = rand;
		this.numberOfIterations = numberOfITerations; 
	}
	
	@Override
	public BitVector run() {
		BitVector bestSolution = new BitVector(rand, satFormula.getNumberOfVariables());
		if(satFormula.isSatisfied(bestSolution)) {
			return bestSolution;
		}
		
		// Hill Climbing
		BitVector solution = null;
		BitVector nextBestSolution = null;
		SATFormulaStats satFormulaStats = new SATFormulaStats(satFormula);
		for(int i = 0; i < numberOfIterations; ++i) {
			int max = -1;
			BitVectorNGenerator generator = new BitVectorNGenerator(bestSolution);
			for(BitVector neighbour: generator) {
				satFormulaStats.setAssignment(neighbour, false);
				int fitness = satFormulaStats.getNumberOfSatisfied();
				if(fitness > max) {
					max = fitness;
					nextBestSolution = neighbour;
				}
			}
			
			if(bestSolution == nextBestSolution) {
				break;
			}
			if(satFormula.isSatisfied(nextBestSolution)) {
				solution = nextBestSolution;
				break;
			}
			
			bestSolution = nextBestSolution;
		}
		
		return solution;
	}

}
