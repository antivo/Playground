package hr.fer.zemris.ml.model;

import Jama.Matrix;

public class IsotropicCovarianceMatrixModel extends Model {
	private final Matrix isotropicCovarianceMatrix;
	private final DiagonalCovarianceMatrixModel diagonalModel;

	public IsotropicCovarianceMatrixModel(DiagonalCovarianceMatrixModel dm) {
		diagonalModel = dm;
		Matrix diagonal = dm.getDiagonalCovarianceMatrix();
		isotropicCovarianceMatrix = new Matrix(diagonal.getRowDimension(), diagonal.getColumnDimension());
		double d = 0;
		for(int i = 0; i < diagonal.getRowDimension(); ++i) {
			d += diagonal.get(i, i);
		}
		d /= diagonal.getRowDimension();
		for(int i = 0; i < diagonal.getRowDimension(); ++i) {
			isotropicCovarianceMatrix.set(i, i, d);
		}
	}
	
	@Override
	public double getGeneralizationError() {
		return getError(diagonalModel.getTest(), diagonalModel.getClassData());
	}

	@Override
	public double getLearningError() {
		return getError(diagonalModel.getTrain(), diagonalModel.getClassData());
	}

	@Override
	protected double calculateClassProbability(Matrix example, String clazz) {
		double n = Math.pow((2 * Math.PI), (double) example.getRowDimension()/2.0);
		n *= Math.pow(isotropicCovarianceMatrix.det(), 0.5);
		
		Matrix temp = example.minus(diagonalModel.getClassData().get(clazz).getExpectedValue());
		double b = temp.transpose().times(isotropicCovarianceMatrix.inverse()).times(temp).get(0, 0);
		b *= -0.5;
		b = Math.exp(b);
		
		return b/n;
	}

	@Override
	public String getAposteriori() {
		return getAposterioriPrettyPrint(diagonalModel.getTest().getOrderedExamples(), diagonalModel.getClassData());
	}

}
