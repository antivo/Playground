package hr.fer.zemris.optjava.threesat.algorithm;

import hr.fer.zemris.optjava.threesat.BitVector;
import hr.fer.zemris.optjava.threesat.BitVectorNGenerator;
import hr.fer.zemris.optjava.threesat.SATFormula;

import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

public class BruteForceAlgorithm implements Algorithm {
	@Override
	public BitVector compute(SATFormula satFormula) {
		BitVector oneSolution = null;
		
		int literals = satFormula.getNumberOfVariables();
		Set<BitVector> visited = new TreeSet<BitVector>();
		Stack<BitVector> toVisit = new Stack<BitVector>();
		toVisit.add(new BitVector(literals));
		
		while(!toVisit.isEmpty()) {
			BitVector bv = toVisit.pop();
			if(!visited.contains(bv)) {
				visited.add(bv);
				
				if(satFormula.isSatisfied(bv)) {
					System.out.println(bv);
					
					if(null == oneSolution) {
						oneSolution = bv;
					}
				}
				
				BitVectorNGenerator generator = new BitVectorNGenerator(bv);
				for(BitVector b : generator) {
					if(!visited.contains(b)) {
						toVisit.add(b);
					}
				}
			}
		}
		
		return oneSolution;
	}
}
