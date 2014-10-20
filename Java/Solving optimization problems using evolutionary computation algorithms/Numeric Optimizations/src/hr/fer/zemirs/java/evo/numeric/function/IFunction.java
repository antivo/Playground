package hr.fer.zemirs.java.evo.numeric.function;

import Jama.Matrix;

public interface IFunction {
	int getVariableNum();
	double f(Matrix vector);
	Matrix grad(Matrix vector);
}
