package hr.fer.zemirs.java.evo.numeric.function;

import Jama.Matrix;


public interface IHFunction extends IFunction {
	Matrix hessian(Matrix vector);
}
