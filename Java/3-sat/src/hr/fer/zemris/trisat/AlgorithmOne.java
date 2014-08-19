package hr.fer.zemris.trisat;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class AlgorithmOne extends Algorithm {

	public AlgorithmOne(SATFormula satFormula) throws IllegalStateException {
		super(satFormula);
	}
	
	@Override
	public BitVector run() {
		BitVector solution = new BitVector(satFormula.getNumberOfVariables());
		
		List<BitVector> newSolutions = new ArrayList<BitVector>();
		List<BitVector> nextSolutions = new ArrayList<BitVector>();
		Set<BitVector> visitedSolutions = new TreeSet<BitVector>();
		
		nextSolutions.clear();
		nextSolutions.add(solution);
		solution = null;
		do {
			newSolutions.clear();
			for(BitVector next: nextSolutions) {
				if(satFormula.isSatisfied(next)) {
					if(null == solution) {
						solution = next;
					}
					System.out.println(next.toString());
				}
			
				BitVectorNGenerator generator = new BitVectorNGenerator(next);
				for(BitVector neighbour: generator) {
					if(!visitedSolutions.contains(neighbour)) {
						newSolutions.add(neighbour);
						visitedSolutions.add(neighbour);
					}
				}
			}
			nextSolutions.clear();
			nextSolutions.addAll(newSolutions);
		} while(0 != nextSolutions.size());
		
		return solution;
	}
}
