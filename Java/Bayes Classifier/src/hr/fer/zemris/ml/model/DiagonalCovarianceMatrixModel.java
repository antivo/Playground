package hr.fer.zemris.ml.model;

import hr.fer.zemris.ml.data.Data;
import hr.fer.zemris.ml.data.PerClassData;

import java.util.Map;

import Jama.Matrix;

public class DiagonalCovarianceMatrixModel extends Model {
	private final Matrix diagonalCovarianceMatrix;
	private final SharedCovarianceMatrixModel sharedModel;

	public DiagonalCovarianceMatrixModel(SharedCovarianceMatrixModel sm) {
		sharedModel = sm;
		Matrix shared = sm.getSharedCovarianceMatrix();
		diagonalCovarianceMatrix = new Matrix(shared.getRowDimension(), shared.getColumnDimension());
		for(int i = 0; i < shared.getRowDimension(); ++i) {
			diagonalCovarianceMatrix.set(i, i, shared.get(i, i));
		}
	}

	@Override
	public double getGeneralizationError() {
		return getError(sharedModel.getTest(), sharedModel.getClassData());
	}

	@Override
	public double getLearningError() {
		return getError(sharedModel.getTrain(), sharedModel.getClassData());
	}

	@Override
	protected double calculateClassProbability(Matrix example, String clazz) {
		double n = Math.pow((2 * Math.PI), (double) example.getRowDimension()/2.0);
		n *= Math.pow(diagonalCovarianceMatrix.det(), 0.5);
		
		Matrix temp = example.minus(sharedModel.getClassData().get(clazz).getExpectedValue());
		double b = temp.transpose().times(diagonalCovarianceMatrix.inverse()).times(temp).get(0, 0);
		b *= -0.5;
		b = Math.exp(b);
		
		return b/n;
	}
	
	public Matrix getDiagonalCovarianceMatrix() {
		return diagonalCovarianceMatrix;
	}

	public Map<String, PerClassData> getClassData() {
		return sharedModel.getClassData();
	}
	
	public Data getTrain() {
		return sharedModel.getTrain();
	}

	public Data getTest() {
		return sharedModel.getTest();
	}
	
	public String getAposteriori() {
		return getAposterioriPrettyPrint(sharedModel.getTest().getOrderedExamples(), sharedModel.getClassData());
	}
}
