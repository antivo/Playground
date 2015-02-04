package hr.fer.zemris.ml.lr.main;

import java.io.IOException;

import hr.fer.zemris.ml.lr.dataset.Dataset;
import hr.fer.zemris.ml.lr.dataset.parser.DatasetParser;
import hr.fer.zemris.ml.lr.dictionary.Dictionary;
import hr.fer.zemris.ml.lr.dictionary.parser.DictionaryParser;
import hr.fer.zemris.ml.lr.gd.BatchGradientDescent;
import hr.fer.zemris.ml.lr.gd.ErrorFunction;

public class Main {
	private static final String tezine_partA = "tezine1.dat ";
	private static final double e_gradientDescent = 0.001;
	
	private static void writeToFile(String filePath, String out) {
		
	}
	
	private static void partA(Dictionary dictionary, Dataset trainigSet) {
		BatchGradientDescent gradientDescend = new BatchGradientDescent(e_gradientDescent, 0.01);
		
		ErrorFunction fun = new ErrorFunction(trainigSet);
		
		int numOfWeights = 1 + dictionary.getNumOfWords();
		double[] w = new double[numOfWeights];
		
		w = gradientDescend.lineSearch(w, fun);

		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < w.length; ++i) {
			String format = String.format("%.2f", w[i]);
			sb.append(format).append("\n");
		}
		sb.append("EE: ").append(fun.getEE(w)).append("\n");
		sb.append("CEE: ").append(fun.getCEE(w));
		
		System.out.println(sb.toString());
	}
	
	public static void main(String[] args) throws IOException {

		
			String dictionaryPath = args[0];
			String trainingSetPath = args[1];
			String validationSetPath = args[2];
			String testSetPath = args[3];
			//String outputDirectoryPath = args[4];
			
			Dictionary dictionary = DictionaryParser.parse(dictionaryPath);
			
			Dataset trainingSet = DatasetParser.parse(trainingSetPath);
			Dataset validationSet = DatasetParser.parse(validationSetPath);
			Dataset testSet = DatasetParser.parse(testSetPath);
		
			partA(dictionary, trainingSet);
	}

}
