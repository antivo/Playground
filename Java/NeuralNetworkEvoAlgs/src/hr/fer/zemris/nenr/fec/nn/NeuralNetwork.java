package hr.fer.zemris.nenr.fec.nn;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.nenr.fec.ds.Dataset;

public class NeuralNetwork {
	private final int weightsLength;
	private final int[] sizePerLayer;
	
	private static void assertNeuralNetwork(int sizePerLayer[]) throws IllegalArgumentException {
		if(sizePerLayer.length < 3) {
			throw new IllegalArgumentException("Neural Network must constist of at least three layers");
		}
		for(int i = 0; i < sizePerLayer.length; ++i) {
			if(sizePerLayer[i] < 1) {
				throw new IllegalArgumentException("Every layer of Neural Network must consist of at leat one neuron");
			}
		}
	}
	
	private static int calculateNeuronOneContribution(int input, int numOfNeuronOne) {
		return (input * 2 *  numOfNeuronOne);
	}
	
	private static int calculateWeightLength(int[] sizePerLayer) {
		int sum = calculateNeuronOneContribution(sizePerLayer[0], sizePerLayer[1]);
		
		for(int i = 2; i < sizePerLayer.length; ++i) {
			int in = sizePerLayer[i-1];
			int current = sizePerLayer[i];
			sum += current * (in + 1);
		}
		return sum;
	}
	
	public NeuralNetwork(int... sizePerLayer) throws IllegalArgumentException {
		assertNeuralNetwork(sizePerLayer);
		
		this.weightsLength = calculateWeightLength(sizePerLayer);
		this.sizePerLayer = sizePerLayer;
	}

	public int getWeightsLength() {
		return weightsLength;
	}
	
	private static void assertInputToNeuralNetwork(int inputArguments, int inputNeurons) throws IllegalArgumentException {
		if(inputArguments != inputNeurons) {
			throw new IllegalArgumentException("Arguments to neural network must be equal to number of input neurons");
		}
	}
	
	private static double neuronOneOutput(double[] weights, int startingIndex, double[] input) {
		double sum = 0;
		for(int i = 0; i < input.length; ++i) {
			int idx = startingIndex + i*2; 
			double b = Math.abs(input[i] - weights[idx]);
			double n = Math.abs(weights[idx + 1]);
			
			sum += b/n;
		}
		
		return (1/(1 + sum));
	}
	
	private static double[] getNeuronOneLayerOutput(double[] weights, double[] input, int layerSize) {
		double[] result = new double[layerSize];
		
		int startingIdx = 0;
		for(int i = 0; i < result.length; ++i) {
			result[i] = neuronOneOutput(weights, startingIdx, input);
			startingIdx += 2 * input.length; 
		}
		
		return result;
	}
	
	private static double neuronTwoOutput(double[] weights, int startingIndex, double[] input) {
		double net = weights[startingIndex++];
		
		for(int i = 0; i < input.length; ++i) {
			net += weights[startingIndex++] * input[i];
		}
		return 1/(1 + Math.exp(-net));
	}
	
	private static double[] getNeuronTwoLayerOutput(double[] weights, double[] input, int layerSize, int startingIndex) {
		double[] result = new double[layerSize];

		for(int i = 0; i < result.length; ++i) {
			result[i] = neuronTwoOutput(weights, startingIndex, input);
			startingIndex += 1 + input.length; 
		}
		
		return result;
	}
	
	public double[] calcOutput(double[] weights, double[] input) throws IllegalArgumentException {
		assertInputToNeuralNetwork(input.length, sizePerLayer[0]);
		
		double[] neuronLayerOutput = getNeuronOneLayerOutput(weights, input, sizePerLayer[1]);
		int idx = calculateNeuronOneContribution(input.length, sizePerLayer[1]);
		
		for(int i = 2; i < sizePerLayer.length; ++i) {
			neuronLayerOutput = getNeuronTwoLayerOutput(weights, neuronLayerOutput, sizePerLayer[i], idx);
			idx += 1 + neuronLayerOutput.length;
		}
		
		return neuronLayerOutput;
	}
	
	public double calcError(Dataset dataset, double[] weights) {
		int N = dataset.getSize();
		int M = sizePerLayer[sizePerLayer.length - 1];
		
		double mse = 0;
		for(int s = 0; s < N; ++s) {
			double[] in = dataset.getInput(s);
			double[] dsOut = dataset.getOutput(s);
			double[] nnOut = calcOutput(weights, in);
			for(int o = 0; o < M; ++o) {
				double t = dsOut[o];
				double y = nnOut[o];
				
				mse += Math.pow((t - y), 2);
			}
		}
		return (mse/N);
	}
	
	private static double[] roundOutputToDiscreeteOutput(double[] o) {
		int length = o.length;
		double[] retValue = new double[length];
		for(int i = 0; i < length; ++i) {
			if(o[i] < 0.5) {
				retValue[i] = 0;
			} else {
				retValue[i] = 1;
			}
		}
		return retValue;
	}
	
	public List<double[]> discreeteOutput(double[] weights, List<double[]> in) {
		List<double[]> out = new ArrayList<double[]>();
		for(double[] input : in) {
			double[] output = calcOutput(weights, input);
			out.add(roundOutputToDiscreeteOutput(output));
		}
		return out;
	}
	
}
