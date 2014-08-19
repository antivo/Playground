package hr.fer.zemris.trisat;

import java.util.Random;

public class AlgorithmThree extends Algorithm  {
	Random rand;
	private final int numberOfIterations;
	private final int numberOfBest;
	private final double percentageConstantUp;
	private final double percentageConstantDown; 
	private final double percentageUnitAmmount;
	
	public AlgorithmThree(SATFormula satFormula, Random rand, int numberOfIterations,
						  int numberOfBest, double percentageConstantUp, double percentageConstantDown, double percentageUnitAmmount)  throws IllegalStateException {
		super(satFormula);
		if (null == rand) {
			throw new IllegalStateException("Algorithm Three request Random Number Generator to be initialized");
		}
		this.rand = rand;
		this.numberOfIterations = numberOfIterations;
		this.numberOfBest = numberOfBest;
		this.percentageConstantUp = percentageConstantUp;
		this.percentageConstantDown = percentageConstantDown;
		this.percentageUnitAmmount = percentageUnitAmmount;
	}

	@Override
	public BitVector run()  {
		BitVector bestSolution = new BitVector(rand, satFormula.getNumberOfVariables());
		if(satFormula.isSatisfied(bestSolution)) {
			return bestSolution;
		}
		
		// Modified Hill Climbing
		BitVector solution = null;
		BitVector nextBestSolution = null;
		SATFormulaStats satFormulaStats = new SATFormulaStats(satFormula, percentageConstantUp, percentageConstantDown);
		for(int i = 0; i < numberOfIterations; ++i) {
			BitVectorNGenerator generator = new BitVectorNGenerator(bestSolution);
			MutableBitVector[] neighbourhood = generator.createNeighbourhood();
			double fitness[] = new double[neighbourhood.length];
			for(int j = 0; j < neighbourhood.length; ++j) {
				BitVector neighbour = neighbourhood[j];
				satFormulaStats.setAssignment(neighbour, false);
				int z = satFormulaStats.getNumberOfSatisfied();
				fitness[j] = z + satFormulaStats.getPercentageBonus() * percentageUnitAmmount;
			}
			int maxIndex = numberOfBest >= neighbourhood.length ? neighbourhood.length : numberOfBest;
			nextBestSolution = neighbourhood[rand.nextInt(maxIndex)];
			
			/*if(bestSolution == nextBestSolution) {
				break;
			}*/
			
			if(satFormula.isSatisfied(nextBestSolution)) {
				solution = nextBestSolution;
				break;
			}
			
			bestSolution = nextBestSolution;
		}
		
		return solution;
	}
	
}
