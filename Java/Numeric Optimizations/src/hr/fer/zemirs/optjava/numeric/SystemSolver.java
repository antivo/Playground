package hr.fer.zemirs.optjava.numeric;

import hr.fer.zemirs.optjava.numeric.algorithms.NumOptAlgorithms;
import hr.fer.zemirs.optjava.numeric.function.IHFunction;

import java.io.IOException;
import java.util.Random;

import Jama.Matrix;

public class SystemSolver {
private static Random rand = new Random();
	
	private static void assertArguments(String[] args) {
		if(args.length != 3 && args.length != 5) {
			throw new RuntimeException("Program expects three arguments: algorithm selection (\"grad\" or \"newton\" ), max number of iterations (integer) and path to file with defined linear system");
		}
	}

	private static Matrix createRanomPoint(int dim) {
		Matrix point = new Matrix(dim, 1);
		for(int i = 0; i < dim; ++i) {
			point.set(i, 0, rand.nextDouble());
		}
		return point;
	}
	
	private static Matrix algotithm(String algorithmSelection, IHFunction linearSystem, int maxIterations, Matrix startingPoint) {
		switch(algorithmSelection) {
		case "grad" : return NumOptAlgorithms.gradientDescentMinimum(linearSystem, maxIterations, startingPoint);
		case "newton" : return NumOptAlgorithms.newtonMethod(linearSystem, maxIterations, startingPoint);
		default : throw new RuntimeException("no such algorithm: " + algorithmSelection);
		}
	}
	
	private static IHFunction system(Parser parser, String systemProblem) {
		switch(systemProblem) {
		case "linear" : return parser.parseLinearSystem();
		case "function" : return parser.parseSystemFunction();
		default: throw new RuntimeException("Unsupported system problem " + systemProblem);
		}
	}
	
	public static void main(String[] args, String systemProblem) throws IOException {
		assertArguments(args);
		String algorithmSelection = args[0];
		String maxIterationSelection = args[1];
		String linearSystemDefinitionPath = args[2];
	
		int maxIterations = Integer.parseInt(maxIterationSelection);
		
		Parser parser = new Parser(linearSystemDefinitionPath);
		IHFunction linearSystem = system(parser, systemProblem);
		int dim = linearSystem.getVariableNum();
		
		Matrix point = createRanomPoint(dim);
		point = algotithm(algorithmSelection, linearSystem, maxIterations, point);
		
		System.out.println("Final solution is:");
		point.print(0, 4);
	}
}
