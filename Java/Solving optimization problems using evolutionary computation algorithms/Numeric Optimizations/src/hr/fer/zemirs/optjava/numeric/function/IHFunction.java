package hr.fer.zemirs.optjava.numeric.function;

import Jama.Matrix;


public interface IHFunction extends IFunction {
	Matrix hessian(Matrix vector);
}
