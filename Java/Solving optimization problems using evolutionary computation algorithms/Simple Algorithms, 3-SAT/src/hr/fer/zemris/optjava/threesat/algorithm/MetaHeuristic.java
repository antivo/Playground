package hr.fer.zemris.optjava.threesat.algorithm;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import hr.fer.zemris.optjava.threesat.BitVector;
import hr.fer.zemris.optjava.threesat.BitVectorNGenerator;
import hr.fer.zemris.optjava.threesat.SATFormula;
import hr.fer.zemris.optjava.threesat.SATFormulaStats;

public class MetaHeuristic implements Algorithm {

	private static final int maxIterations = 100000;
	private static final int numberOfBest = 2;

	private Random rand;

	public MetaHeuristic(Random rand) {
		this.rand = rand;
	}

	@Override
	public BitVector compute(SATFormula satFormula) {
		SATFormulaStats satFormulaStats = new SATFormulaStats(satFormula);	
		BitVector oneSolution = null;
		int numberOfVariables = satFormula.getNumberOfVariables();
		if(0 == numberOfVariables) return null;
		BitVector test = new BitVector(rand, numberOfVariables);
		
		int iterations = maxIterations;
		while(iterations-- > 0) {
			satFormulaStats.setAssigment(test, true);
			if(satFormulaStats.issSatisfied()) {
				System.out.println(test);
				break;
			}
			
			BitVectorNGenerator generator = new BitVectorNGenerator(test);
			BitVector[] neighborhood = generator.craeteNeighborhood(); 
			Map<BitVector, Double> fit = new TreeMap<BitVector, Double>();
			for(int i = 0; i < neighborhood.length; ++i) {
				BitVector trial = neighborhood[i];
				satFormulaStats.setAssigment(trial, false);
				double fitness = satFormulaStats.getNumberOfSatisfied() + satFormulaStats.getPercentageBonus();
				fit.put(trial, fitness);
			}
			Arrays.sort(neighborhood, 0, neighborhood.length, new Comparator<BitVector>() {
				@Override
				public int compare(BitVector lhs, BitVector rhs) {
					double f1 = fit.get(lhs);
					double f2 = fit.get(rhs);
					int fromGreater = -Double.compare(f1, f2);
					return fromGreater;
				}				
			});
			int selected = rand.nextInt(numberOfBest);
			test = neighborhood[selected];
		}
		if(satFormula.isSatisfied(test)) {
			oneSolution = test;
		}
		return oneSolution;
	}

}
