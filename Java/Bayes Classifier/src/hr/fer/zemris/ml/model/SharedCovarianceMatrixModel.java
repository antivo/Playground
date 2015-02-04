package hr.fer.zemris.ml.model;

import java.util.Map;

import hr.fer.zemris.ml.data.Data;
import hr.fer.zemris.ml.data.PerClassData;
import Jama.Matrix;

public class SharedCovarianceMatrixModel extends Model {
	private final Matrix sharedCovarianceMatrix;
	private final GeneralModel generalModel;
	
	private static Matrix calculateSharedCovariance(GeneralModel gm) {
		Matrix sharedCovMat = null;
		for(String clazz : gm.getClassData().keySet()) {
			PerClassData pcd = gm.getClassData().get(clazz);
			if(sharedCovMat == null) {
				sharedCovMat = pcd.getCovarianceMatrix();
			} else {
				sharedCovMat = sharedCovMat.plus(pcd.getCovarianceMatrix());
			}
		}
		return sharedCovMat;
	}
	
	public SharedCovarianceMatrixModel(GeneralModel gm) {
		this.generalModel = gm;
		sharedCovarianceMatrix = calculateSharedCovariance(gm);
	}

	@Override
	public double getGeneralizationError() {
		return getError(generalModel.getTest(), generalModel.getClassData());
	}

	@Override
	public double getLearningError() {
		return getError(generalModel.getTrain(), generalModel.getClassData());
	}

	@Override
	protected double calculateClassProbability(Matrix example, String clazz) {
		double n = Math.pow((2 * Math.PI), (double) example.getRowDimension()/2.0);
		n *= Math.pow(sharedCovarianceMatrix.det(), 0.5);
		
		Matrix temp = example.minus(generalModel.getClassData().get(clazz).getExpectedValue());
		double b = temp.transpose().times(sharedCovarianceMatrix.inverse()).times(temp).get(0, 0);
		b *= -0.5;
		b = Math.exp(b);
		
		return b/n;
	}

	public Matrix getSharedCovarianceMatrix() {
		return sharedCovarianceMatrix;
	}

	public Map<String, PerClassData> getClassData() {
		return generalModel.getClassData();
	}
	
	public Data getTrain() {
		return generalModel.getTrain();
	}

	public Data getTest() {
		return generalModel.getTest();
	}

	@Override
	public String getAposteriori() {
		return getAposterioriPrettyPrint(generalModel.getTest().getOrderedExamples(), generalModel.getClassData());
	}
}
