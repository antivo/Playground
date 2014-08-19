package hr.fer.zemris.optjava.no.function;

import Jama.Matrix;

public class Point {
	private Matrix point;
	
	public double get(int i) {
		return point.get(i, 0);
	}
	
	void assertPoint(Matrix point) throws IllegalArgumentException {
		if(1 != point.getRowDimension()) {
			throw new IllegalArgumentException("Point is one dimensional matrix");
		}
	}
	
	public Point(Matrix point) throws IllegalArgumentException {
		assertPoint(point);
		this.point = point;
	}
	
	public Point(double... coordinates) {
		this.point = new Matrix(coordinates, 1);
	}
	
	public Matrix getAsMatrix() {
		return point;
	}
	
	public double[] getAsArray() {
		return point.getRowPackedCopy();
	}
	
	public int getDimension() {
		return point.getColumnDimension();
	}
}
