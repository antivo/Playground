package hr.fer.zemirs.optjava.numeric.function;

import Jama.Matrix;

public class LinearSystem extends AbstractSystem implements IHFunction {
	public LinearSystem(Matrix a, Matrix b) {
		super(a,b);
	}

	@Override
	public int getVariableNum() {
		return A.getRowDimension();
	}

	private Matrix offset(Matrix vector) {
		return A.times(vector).minus(b);
	}
	
	@Override
	public double f(Matrix vector) {
		Matrix F = offset(vector);
		double f = 0;
		for(int i = 0; i < F.getRowDimension(); ++i) {
			double elem = F.get(i, 0);
			f += elem*elem;
		}
		return f;
	}

	private Matrix transposeA = null;
	private Matrix H = null;
	
	@Override
	public Matrix grad(Matrix vector) {
		if(transposeA == null) {
			transposeA = A.transpose();
		}
		return transposeA.times(2).times(offset(vector));
	}
	
	@Override
	public Matrix hessian(Matrix vector) {
		if(null == H) {
			if(transposeA == null) {
				transposeA = A.transpose();
			}
			H = A.transpose().times(A).times(2);
		}
		return H;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < A.getRowDimension(); ++i) {
			sb.append('[');
			for(int j = 0; j < A.getColumnDimension(); ++j) {
				double elem = A.get(i, j);
				sb.append(Double.toString(elem));
				sb.append("\t");
			}
			sb.append(']');
			sb.append(" [ x" + (i+1) + " ] = [ " + Double.toString(b.get(i,0)) + " ]" );
			sb.append("\n");
		}
		return sb.toString();
	}
}
