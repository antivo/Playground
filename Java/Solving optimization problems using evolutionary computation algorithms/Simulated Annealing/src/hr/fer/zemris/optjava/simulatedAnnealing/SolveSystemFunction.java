package hr.fer.zemris.optjava.simulatedAnnealing;

import hr.fer.zemirs.optjava.numeric.Parser;
import hr.fer.zemirs.optjava.numeric.function.IFunction;
import hr.fer.zemris.optjava.simulatedAnnealing.solution.BitVectorSolution;
import hr.fer.zemris.optjava.simulatedAnnealing.solution.DoubleArraySolution;
import hr.fer.zemris.optjava.simulatedAnnealing.solution.SingleObjectiveSolution;

import java.io.IOException;
import java.util.Random;

import Jama.Matrix;

public class SolveSystemFunction {
	
	private static Random rand = new Random();
	private static int dim;
	private static double[] maxs = new double[] {10, 0, 3, 2, 1, 4};
	private static double[] mins = new double[] {7, -5, 0, 0, 0, 2};
	private static double[] deltas = new double[] {1e-2, 1e-3, 1e-4, 1e-4, 1e-4, 1e-3};
	
	private static Class<?> determineRepresentation(String solutionRepresentation) {
		switch(solutionRepresentation) {
		case "decimal": return DoubleArraySolution.class;
		case "binary": return BitVectorSolution.class;
		}
		throw new RuntimeException("Unsupported solution representation " + solutionRepresentation);
	}
	
	private static Class<?> determineAlgorithm(String algorithm) {
		switch(algorithm) {
		case "greedy": return GreedyAlgorithm.class;
		case "simulatedAnnealing": return SimulatedAnnealing.class;
		}
		throw new RuntimeException("Unsupported algorithm " + algorithm);
	}
	
	private static Ifunction makeProblem(String linearSystemDefinitionPath) throws IOException {
		Parser parser = new Parser(linearSystemDefinitionPath);
		IFunction linearSystem = parser.parseSystemFunction();
		dim = linearSystem.getVariableNum();
		
		Ifunction problem = new Ifunction() {
			@Override
			public double valueAt(double[] point) {
				Matrix p = new Matrix(point, dim);
				return linearSystem.f(p);
			}
		};
		
		/*Ifunction problem = new Ifunction() {
			@Override
			public double valeAt(double[] point) {
				return Math.abs(point[0]) + Math.abs(point[1]);
			}
		};
		dim = 2;*/
		
		return problem;
	}
	
	// "First argument expected - algorithm selection"
	// "Second argument expected - solution representation selection"
	public static void main(String[] args) throws IOException {
		Ifunction function = makeProblem(args[0]);
		Class<?> algorithm = determineAlgorithm(args[1]);
		Class<?> representation = determineRepresentation(args[2].split(":")[0]);

		AlgorithmsTemplate template = new AlgorithmsTemplate(rand, dim, maxs, mins, deltas);
		
		SingleObjectiveSolution solution = null;
		if(algorithm == GreedyAlgorithm.class && representation == BitVectorSolution.class) {
			solution = template.runGreedyBinary(function, args[2].split(":")[1], args[3], null, args[6], args[7]);
		} else if(algorithm == GreedyAlgorithm.class && representation == DoubleArraySolution.class) {
			solution = template.runGreedyDouble(function, args[3], null, args[4], args[5]);
		} else if(algorithm == SimulatedAnnealing.class && representation == BitVectorSolution.class) {
			solution = template.runSimulatedAnnealingBinary(function, args[2].split(":")[1], args[3], null, args[4], args[5], args[6], args[7], args[8]);
		} else if(algorithm == SimulatedAnnealing.class && representation == DoubleArraySolution.class) {
			solution = template.runSimulatedAnnealingDouble(function, args[3], null, args[4], args[5], args[6], args[7], args[8]);
		}
		
		System.out.println("Final Solution is: \n" + solution);
	}
}
