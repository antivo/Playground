package hr.fer.zemirs.java.evo.numeric;

import hr.fer.zemirs.java.evo.numeric.algorithms.NumOptAlgorithms;
import hr.fer.zemirs.java.evo.numeric.function.FOne;
import hr.fer.zemirs.java.evo.numeric.function.FTwo;
import hr.fer.zemirs.java.evo.numeric.function.IHFunction;
import Jama.Matrix;

public class Simple {

	private static IHFunction determineProblem(char assigment) {
		switch (assigment) {
		case '1' : return new FOne();
		case '2' : return new FTwo();
		}
		throw new RuntimeException(assigment + " is not valid choice for a problem (function)");
	}
	
	private static Matrix determineStartingPoint(String firstCoord, String secondCoord) {
		Matrix startingPoint = null;
		if(null != firstCoord && null != secondCoord) {
			double x = Double.parseDouble(firstCoord);
			double y = Double.parseDouble(secondCoord);
			startingPoint = new Matrix(2, 1);
			startingPoint.set(0, 0, x);
			startingPoint.set(1, 0, y);
		}
		return startingPoint;
	}
	
	private static void assertArgumentLength(String[] args) {
		if(args.length != 2 && args.length != 4) {
			throw new RuntimeException("Expected number of argumets is: 2 or 4");
		}
	}
	
	private static void assertProblemAssigmentLength(String problemAssigment) {
		if(problemAssigment.length() != 2) {
			throw new RuntimeException("Problem assigment argument length is 2. 1 or 2 to select fucntion followed by a or b to select gradient descent or newton method");
		}  
	}
	
	private static Matrix findSolution(char methodSelection, IHFunction problem, int maxIterations, Matrix startingPoint) {
		switch(methodSelection) {
		case 'a': return NumOptAlgorithms.gradientDescentMinimum(problem, maxIterations, startingPoint);
		case 'b': return NumOptAlgorithms.newtonMethod(problem, maxIterations, startingPoint);
		}
		throw new RuntimeException(methodSelection + " is not valid choice for a method");
	}
	
	public static void main(String[] args) {
		assertArgumentLength(args);
		String problemAssigment = args[0];
		
		assertProblemAssigmentLength(problemAssigment);
		char problemSelection = problemAssigment.charAt(0);
		IHFunction problem = determineProblem(problemSelection);
		String maxIterationsAssigment = args[1];
				
		String firstCoordinateAssigment = null;
		String secondCoordinateAssigment = null;
		if(args.length == 4) {
			firstCoordinateAssigment = args[2];
			secondCoordinateAssigment = args[3];
		}
		int maxIterations = Integer.parseInt(maxIterationsAssigment);
		Matrix startingPoint = determineStartingPoint(firstCoordinateAssigment, secondCoordinateAssigment);
		
		char methodSelection = problemAssigment.charAt(1);
		Matrix solution = findSolution(methodSelection, problem, maxIterations, startingPoint);	
		if(null != solution) {
			System.out.println("Final solution is");
			solution.print(10, 4);
		} else {
			System.out.println("there was no final solution");
		}
	}

}
