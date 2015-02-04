package hr.fer.zemris.optjava.threesat;

import hr.fer.zemris.optjava.threesat.algorithm.Algorithm;
import hr.fer.zemris.optjava.threesat.algorithm.BruteForceAlgorithm;
import hr.fer.zemris.optjava.threesat.algorithm.IterativeHillClimbing;
import hr.fer.zemris.optjava.threesat.algorithm.MetaHeuristic;

import java.io.IOException;
import java.util.Random;

public class ThreeSATSolver {

	private static Random rand = new Random();
	
	private static Algorithm determineAlgorithm(int selection ) {
		switch(selection) {
		case 1: return new BruteForceAlgorithm();
		case 2: return new IterativeHillClimbing(rand);
		case 3: return new MetaHeuristic(rand);
		}
		throw new RuntimeException("No algorithm under choice " + selection);
	}
	
	public static void main(String[] args) throws IOException {
		String input = args[0];
		int algorithmSelection = Integer.parseInt(args[1]);
		Parser parser = new Parser(input, "\\s+");
		SATFormula satFormula = parser.parse();
		System.out.println("3-SAT: " + satFormula);
		
		Algorithm algorithm = determineAlgorithm(algorithmSelection);
		BitVector solution = algorithm.compute(satFormula);
		
		if(null != solution) {
			System.out.println("Found solution: " + solution);
		} else {
			System.out.println("No solution was found. Better luck next time.");
		}
	}

}
