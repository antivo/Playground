package hr.fer.zemris.trisat;

import java.util.Random;

public class TriSATSolver {

	public static void main(String[] args) {		
		if(args.length != 2) {
			System.out.println("Two parameters are demanded: algorithm number and filename");
			System.exit(1);
		}
		
		int algorithmChoice = Integer.parseInt(args[0]);
		if(algorithmChoice < 1 || algorithmChoice > 3) {
			System.out.println("algorithm choice must be from {1, 2, 3}");
			System.exit(1);
		}

		Parser parser = null;
		try {
			parser = new Parser(args[1]);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}

		SATFormula satFormula = parser.getSATFormula();
		
		Algorithm algorithm = null;
		Random rand = null;
		try {
			switch(algorithmChoice) {
			case 1: {
				algorithm = new AlgorithmOne(satFormula);
				break;
			}
			case 2: {
				rand = new Random();
				int numberOfIterations = 1000;
				algorithm = new AlgorithmTwo(satFormula, rand, numberOfIterations);
				break;
			}
			case 3: {
				rand = new Random();
				int numberOfIterations = 1000;
				int numberOfBest = 2;
				double percentageConstantUp = 0.01;
				double percentageConstantDown = 0.1;
				double percentageUnitAmount = 50.;
				algorithm = new AlgorithmThree(satFormula, rand, numberOfIterations, numberOfBest, percentageConstantUp, percentageConstantDown, percentageUnitAmount);
				break;
			}
			}
		} catch (IllegalStateException e) {
			System.out.println("Algorithm was not initialized. " + e.toString());
			System.exit(1);
		}	
		
		BitVector solution = null;
		solution = algorithm.run();
		
		
		if (null == solution) {
			System.out.println("Algorithm could not find solution given parameters, constraints and data.");
		} else {
			System.out.println("Solution: " + solution.toString());
		}
	}
}
