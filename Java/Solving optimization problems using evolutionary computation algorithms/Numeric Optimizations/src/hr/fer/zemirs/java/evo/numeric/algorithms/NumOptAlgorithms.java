package hr.fer.zemirs.java.evo.numeric.algorithms;

import hr.fer.zemirs.java.evo.numeric.function.IFunction;
import hr.fer.zemirs.java.evo.numeric.function.IHFunction;
import Jama.Matrix;

public class NumOptAlgorithms {
	
	static private final double lower = 0f;
	static private final double startingUpper = 1e-6;
	static private final double step = 2;
	static private final double epsilon = 1e-9;
	//static private final double startingLambda = 1; 
	static private final double delta = 1e-12;
	
	/**
	 * 
	 * @param problem - function for minimization
	 * @param point - starting point
	 * @param descent - opposite direction of gradient
	 * @return
	 */
	private static double findUpper(IFunction problem, Matrix point, Matrix descent) {
		double upper = startingUpper;
		double limit = problem.f(point);
		double current = limit ;
		do {
			upper *= step;
			Matrix newPoint = descent.times(upper).plus(point); // descent * upper + point
			current = problem.f(newPoint);
		} while (limit > current);
		return upper;
	}
	
	/**
	 * 
	 * @param problem - function for minimization
	 * @param point - starting point
	 * @param descent - opposite direction of gradient
	 * @return
	 */
	private static double findLambdaBisection(IFunction problem, Matrix point, Matrix descent) {
		double l = lower;
		double u = findUpper(problem, point, descent); 
		double lambda = (l + u) / 2.0;
		while(u - l < epsilon) {
			Matrix newPoint = descent.times(lambda).plus(point); // descent * lambda + point
			Matrix grad = problem.grad(newPoint); // gradient at new point
			double value =  0; 
			for(int i = 0; i < problem.getVariableNum(); ++i) {// dot product
				value += grad.get(i, 0) * descent.get(i, 0);
			}
		
			// 
			if (value > 0){
				u = lambda;
			} else {
				l = lambda;
			}
			lambda = (l + u) / 2.0;
		}
		return lambda;
	}
	
	public static Matrix gradientDescentMinimum(IFunction problem, int maxIteration, Matrix point) {
		int iter = 0;		
		System.out.println(problem);
		point.print(0, 4);
		double value = delta;
		while(maxIteration > iter++ && value >= delta) {
			System.out.println("ITER: " + iter);
			Matrix descent = problem.grad(point).times(-1); // -1 * grad
			//System.out.println("DESCENT: ");
			//descent.print(1, 1);
			double lambda = findLambdaBisection(problem, point, descent);
			//System.out.println("lambda " + lambda);
			value = problem.f(point);
			point = descent.times(lambda).plus(point);
			value -= problem.f(point);
			//System.out.println("point");
			point.print(0, 4);
		}
		return point;
		
	}
	
	public static Matrix newtonMethod(IHFunction problem, int maxIteration, Matrix point) {
		int iter = 0;
		System.out.println(problem);
		point.print(0, 4);
		double value = delta;
		while(maxIteration > iter++  && value >= delta) {
			System.out.println("ITER: " + iter);
			Matrix invH = problem.hessian(point).inverse();
			Matrix g = problem.grad(point);
			Matrix descent = invH.times(g).times(-1); // - H^(-1) * grad
			value = problem.f(point);
			double lambda = findLambdaBisection(problem, point, descent);
			point = descent.times(lambda).plus(point);
			value -= problem.f(point);
			point.print(0, 4);
		}
		return point;
	}
}
