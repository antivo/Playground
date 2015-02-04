package hr.fer.zemris.ml.data;

import java.util.List;

import Jama.Matrix;

public class PerClassData {
	private final Matrix expectedValue;
	private final Matrix covarianceMatrix;
	private final double classProbability;
	
	public Matrix getExpectedValue() {
		return expectedValue;
	}

	public Matrix getCovarianceMatrix() {
		return covarianceMatrix;
	}

	public double getClassProbability() {
		return classProbability;
	}

	public PerClassData(List<Matrix> x, int n) {
		expectedValue = calculateExpectedValue(x);
		covarianceMatrix = calculateCovarianceMatrix(x, expectedValue);
		classProbability = calculateClassProbability(x.size(), n);
	}
	
	private static Matrix calculateExpectedValue(List<Matrix> x) {
		Matrix result = null;
		if(x.size() > 0) {
			int length = x.get(0).getRowDimension();
			double[] expectation = new double[length];
			for(Matrix m : x) {
				for(int i = 0; i < length; ++i) {
					expectation[i] += m.get(i, 0);
				}
			}
			double Nj = x.size();
			for(int i = 0; i < expectation.length; ++i) {
				expectation[i] /= Nj;
			}
			result = new Matrix(expectation, expectation.length);
		}
		return result;
	}
	
	private static Matrix calculateCovarianceMatrix(List<Matrix> x, Matrix expectedValue) {
		Matrix covariance = null;
		if(x.size() > 0) {
			int length = x.get(0).getRowDimension();
			covariance = new Matrix(length, length);
			for(Matrix m : x) {
				Matrix element = m.minus(expectedValue);
				Matrix transposedElement = element.transpose();
				Matrix temp = element.times(transposedElement);
				
				covariance = covariance.plus(temp);
			}
			
			double normalize = 1./x.size();
			covariance = covariance.times(normalize);
		}
		return covariance;
	}
	
	private static double calculateClassProbability(int Nj, int N) {
		double classProbability = (double) Nj / N;
		return classProbability;
	}
}
