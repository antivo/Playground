package hr.fer.zemirs.java.evo.numeric.function;

import Jama.Matrix;

public class FOne implements IHFunction {

	@Override
	public int getVariableNum() {
		return 2;
	}

	@Override
	public double f(Matrix vector) {
		double x1 = vector.get(0, 0);
		double x2 = vector.get(1,0);
		
		return x1 * x1 + (x2 - 1) * (x2 - 1);
	}

	@Override
	public Matrix grad(Matrix vector) {
		double x1 = vector.get(0, 0);
		double x2 = vector.get(1,0);
		
		Matrix g = new Matrix(2, 1);
		g.set(0, 0, 2 * x1);
		g.set(1, 0, 2 * (x2 - 1));
		
		return g;
	}

	@Override
	public Matrix hessian(Matrix vector) {
		Matrix h = new Matrix(2, 2);
		h.set(0, 0, 2);		h.set(0, 1, 0);
		h.set(1, 0, 0);		h.set(1, 1, 2);
		return h;
	}

	@Override
	public String toString() {
		return "f1(x1, x2) = x1^2 + (x2-1)^2";
	}
}
