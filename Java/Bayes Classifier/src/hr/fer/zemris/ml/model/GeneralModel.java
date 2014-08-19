package hr.fer.zemris.ml.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import Jama.Matrix;
import hr.fer.zemris.ml.data.Data;
import hr.fer.zemris.ml.data.PerClassData;

public class GeneralModel extends Model {
	private final Set<String> classSet;
	private final Map<String, PerClassData> classData;
	private final Data train;
	private final Data test;
	
	
	
	public GeneralModel(Data train, Data test) {
		this.classSet = train.getClassEntries().keySet();
		this.classData = new HashMap<String, PerClassData>();
		for(String key : this.classSet) {
			List<Matrix> examples = train.getClassEntries().get(key);
			PerClassData perClassData = new PerClassData(examples, train.getN());
			this.classData.put(key, perClassData);
		}
		this.train = train;
		this.test = test;
		
		/*for(String s : classSet) {
			System.out.println(s);
			//classData.get(s).getCovarianceMatrix().print(0, 10);;
			System.out.println(classData.get(s).getClassProbability());
		}*/
	}
	
	public double getGeneralizationError() {
		return getError(test, classData);
	}
	
	public double getLearningError() {
		return getError(train, classData);
	}
	
	protected double calculateClassProbability(Matrix example, String clazz) {
		double n = Math.pow((2 * Math.PI), (double) example.getRowDimension()/2.0);
		n *= Math.pow(classData.get(clazz).getCovarianceMatrix().det(), 0.5);
		
		Matrix temp = example.minus(classData.get(clazz).getExpectedValue());
		double b = temp.transpose().times(classData.get(clazz).getCovarianceMatrix().inverse()).times(temp).get(0, 0);
		b *= -0.5;
		b = Math.exp(b);
		
		return b/n;
	}

	public String getAposteriori() {
		return getAposterioriPrettyPrint(test.getOrderedExamples(), classData);
	}

	public Map<String, PerClassData> getClassData() {
		return classData;
	}

	public Data getTrain() {
		return train;
	}

	public Data getTest() {
		return test;
	}
	
}
