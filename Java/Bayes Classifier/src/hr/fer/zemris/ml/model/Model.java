package hr.fer.zemris.ml.model;

import hr.fer.zemris.ml.data.Data;
import hr.fer.zemris.ml.data.PerClassData;

import java.util.List;
import java.util.Map;
import java.util.Set;

import Jama.Matrix;

public abstract class Model {
	abstract public double getGeneralizationError();
	
	abstract public double getLearningError();
	
	abstract protected double calculateClassProbability(Matrix example, String clazz);

	protected double getError(Data dataSet, Map<String, PerClassData> classData) {
		int NUM = 0;
		int ERR = 0;
		
		for(String clazz : dataSet.getClassEntries().keySet()) {
			List<Matrix> ml = dataSet.getClassEntries().get(clazz);
			for(Matrix m : ml) {
				++NUM;
				double max = 0;
				String bestGuess = "";
				double n = 0;
				for(String possibleClass : dataSet.getClassEntries().keySet()) {
					double p = calculateClassProbability(m, clazz);
					//System.out.println(p);
					p *= classData.get(possibleClass).getClassProbability();
					n += p;
				}
				for(String possibleClass : dataSet.getClassEntries().keySet()) {
					double p = calculateClassProbability(m, clazz);
					//System.out.println(p);
					p *= classData.get(possibleClass).getClassProbability();
					p /= n;
					if(max < p) {
						max = p;
						bestGuess = possibleClass;
					}
				}
				if(!bestGuess.equals(clazz)) {
					/*System.out.println(clazz);
					System.out.println(bestGuess);*/
					++ERR;
				}
			}
		}
		return (double) ERR / (double) NUM;
	}
	
	protected String getAposterioriPrettyPrint(List<Matrix> examples, Map<String, PerClassData> classData) {
		Set<String> classSet = classData.keySet();
		StringBuilder sb = new StringBuilder("");
		
		for(String s : classSet) {
			sb.append(s).append("\t");
		}
		sb.append("klasa").append("\n");
		
		double[] values = new double[classSet.size()];
		for(Matrix matrix : examples) {
		 	double n = 0;
			String answer = "";
			double max = 0;
			int idx = 0;
			for(String clazz : classSet) {
				double b = calculateClassProbability(matrix, clazz);
				b *= classData.get(clazz).getClassProbability();
				values[idx++] = b;
				n += b;
				if(max < b) {
					max = b;
					answer = clazz;
				}
			}
			for(int i = 0; i < values.length; ++i) {
				values[i] /= n;
				//System.out.println(values[i]);
			}
			
			for(int i = 0; i < values.length; ++i) {
				sb.append(String.format("%.2f", values[i])).append("\t");
			}
			sb.append(answer).append("\n");
		}
		
		return sb.toString().replace(',', '.');
	}
	
	abstract public String getAposteriori();
}
