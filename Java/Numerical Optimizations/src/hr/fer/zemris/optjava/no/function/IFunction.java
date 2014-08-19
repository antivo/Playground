package hr.fer.zemris.optjava.no.function;

import Jama.Matrix;

public interface IFunction {
	int getVariableNumber();
	double getValueAt(Point point);
	Matrix getGradientAt(Point point);
}
