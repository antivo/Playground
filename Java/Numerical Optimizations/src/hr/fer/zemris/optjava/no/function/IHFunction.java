package hr.fer.zemris.optjava.no.function;

import Jama.Matrix;

public interface IHFunction extends IFunction {
	Matrix getHesseMatrix(Point point);
}
