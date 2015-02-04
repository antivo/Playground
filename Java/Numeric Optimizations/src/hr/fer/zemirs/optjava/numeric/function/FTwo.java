package hr.fer.zemirs.optjava.numeric.function;

import Jama.Matrix;

public class FTwo implements IHFunction {

	@Override
	public int getVariableNum() {
		return 2;
	}

	@Override
	public double f(Matrix vector) {
		double x1 = vector.get(0, 0);
		double x2 = vector.get(1,0);
		
		return (x1 - 1) * (x1 - 1) + 10 * (x2 - 2) * (x2 - 2);
	}

	@Override
	public Matrix grad(Matrix vector) {
		double x1 = vector.get(0, 0);
		double x2 = vector.get(1,0);
		
		Matrix g = new Matrix(2, 1);
		g.set(0, 0, 2 * (x1 - 1));
		g.set(1, 0, 20 * (x2 - 2));
		return g;
	}

	@Override
	public Matrix hessian(Matrix vector) {
		Matrix h = new Matrix(2,2);
		h.set(0, 0, 2);		h.set(0, 1, 0);
		h.set(1, 0, 0);		h.set(1, 1, 20);
		return h;
	}

	@Override
	public String toString() {
		return "f2(x1, x2) = (x1 - 1)^2 + 10 * (x2 - 2)^2";
	}
	
}
