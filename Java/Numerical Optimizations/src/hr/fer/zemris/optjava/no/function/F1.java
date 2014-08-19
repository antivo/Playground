package hr.fer.zemris.optjava.no.function;

import Jama.Matrix;

public class F1 implements IHFunction {
	private static final int variableNumber = 2;
	
	private double getX1(Point point) {
		return point.get(0);
	}
	
	private double getX2(Point point) {
		return point.get(2);
	}

	@Override
	public int getVariableNumber() {
		return variableNumber;
	}

	@Override
	public double getValueAt(Point point) {
		double x1 = getX1(point);
		double x2 = getX2(point);

		double first = Math.pow(x1, 2);
		double second = Math.pow((x2 - 1), 2);
		
		double value = first + second;
		return value;
	}

	private static double[] makeGradient(double x1, double x2) {
		return new double[] {2 * x1, 2 * (x2 - 1)};
	}
	
	@Override
	public Matrix getGradientAt(Point point) {
		double x1 = getX1(point);
		double x2 = getX2(point);
		
		double[] gradient = makeGradient(x1, x2);

		Matrix result = new Matrix(gradient, 1);
		return result;
	
	}

	@Override
	public Matrix getHesseMatrix(Point point) {
		double[][] hesse = new double[][] { { 2.0 , 0.0 },
						               	    { 0.0 , 2.0 } };
		
		Matrix hesseMatrix = new Matrix(hesse);
		return hesseMatrix;
	}
}
