package hr.fer.zemris.nenr.fec.main;

import hr.fer.zemris.nenr.fec.ds.Dataset;
import hr.fer.zemris.nenr.fec.ds.Parser;
import hr.fer.zemris.nenr.fec.nn.NeuralNetwork;
import hr.ger.zemris.nenr.fec.ga.DefinedEvaluator;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import ClonalSelectionAlgorithm.Antibody;
import ClonalSelectionAlgorithm.ClonAlg;
import DifferentialEvolution.DiffEvo;
import DifferentialEvolution.Individual;

public class MainDiffEvo {
	
	private static Individual getOptimalWeights(Dataset dataset, NeuralNetwork neuralNetwork) {
		DefinedEvaluator evaluator = new DefinedEvaluator(neuralNetwork, dataset);
		int chromosomeLength = neuralNetwork.getWeightsLength();
		DiffEvo diffEvo = new DiffEvo(evaluator, chromosomeLength);
		
		Individual bestIndividual = diffEvo.train();
		
		System.out.println(evaluator.calcError(bestIndividual.get()));
		return bestIndividual;
	}
	
	private static StringBuilder addToOutput(StringBuilder sb, double[] out) {
		for(int i = 0; i < out.length; ++i) {
			sb.append(String.format("%.2f", out[i])).append(" ");
		}
		return sb;
	}
	
	private static void printFinalResults(Dataset dataset, NeuralNetwork neuralNetwork, double[] bestWeights) {
		List<double[]> commonIn = dataset.getInput();
		List<double[]> expectedOut = dataset.getOutput();
		List<double[]> netOut = neuralNetwork.discreeteOutput(bestWeights, commonIn);

		
		int incorrectlyClassified = 0;
		for(int i = 0; i < commonIn.size(); ++i) {
			double[] in = commonIn.get(i);
			double[] dsOut = expectedOut.get(i);
			double[] nnOut = netOut.get(i);
			
			StringBuilder sb = new StringBuilder();
			sb = addToOutput(sb, in);
			sb = addToOutput(sb, dsOut);
			sb = addToOutput(sb, nnOut);
			System.out.println(sb.toString());
			
			for(int j = 0; j < dsOut.length; ++j) {
				if(dsOut[j] != nnOut[j]) {
					++incorrectlyClassified;
					break;
				}
			}
		}
		
		double correctlyClassified = (1 - (double)incorrectlyClassified/(double)commonIn.size())*100;
		System.out.println("Corectly Classified " + String.format("%.5f", correctlyClassified) + " %");
	}
	
	public static void main(String[] args) throws IOException {
		Parser parser = new Parser();

		Dataset ds = parser.parse2D3Class(args[0]);
		int[] neuralNetworkDim = new int[args.length - 1];
		for(int i = 1; i < args.length; ++i) {
			int arg = Integer.parseInt(args[i]);
			neuralNetworkDim[i-1] = arg;
		}
		NeuralNetwork neuralNetwork = new NeuralNetwork(neuralNetworkDim);
		
		Individual bestAntibody = getOptimalWeights(ds, neuralNetwork);
		double[] bestWeights = bestAntibody.get();
		
		printFinalResults(ds, neuralNetwork, bestWeights);

	}
}
