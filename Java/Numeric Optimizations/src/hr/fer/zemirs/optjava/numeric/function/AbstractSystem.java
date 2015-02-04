package hr.fer.zemirs.optjava.numeric.function;

import Jama.Matrix;

public abstract class AbstractSystem {
	protected final Matrix A;
	protected final Matrix b;

	static private void assertRightSide(Matrix b) {
		if(b.getColumnDimension() != 1) {
			throw new RuntimeException("Right side matrix must be vector");
		}
	}
	
	public AbstractSystem(Matrix a, Matrix b) {
		assertRightSide(b);
		this.A = a;
		this.b = b;
	}
	
	@Override
	public abstract String toString();
}
