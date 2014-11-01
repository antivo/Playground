package hr.fer.zemris.optjava.simulatedAnnealing;

import hr.fer.zemris.optjava.simulatedAnnealing.decoder.GrayBinaryDecoder;
import hr.fer.zemris.optjava.simulatedAnnealing.decoder.IDecoder;
import hr.fer.zemris.optjava.simulatedAnnealing.decoder.NaturalBinaryDecoder;
import hr.fer.zemris.optjava.simulatedAnnealing.decoder.PassThroughDecoder;
import hr.fer.zemris.optjava.simulatedAnnealing.neighborhood.BitVectorByOneNeighborhood;
import hr.fer.zemris.optjava.simulatedAnnealing.neighborhood.DoubleArrayNormNeighborhood;
import hr.fer.zemris.optjava.simulatedAnnealing.neighborhood.DoubleArrayUnifNeighborhood;
import hr.fer.zemris.optjava.simulatedAnnealing.neighborhood.INeighborhood;
import hr.fer.zemris.optjava.simulatedAnnealing.solution.BitVectorSolution;
import hr.fer.zemris.optjava.simulatedAnnealing.solution.DoubleArraySolution;
import hr.fer.zemris.optjava.simulatedAnnealing.tempSchedule.GeometricTempSchedule;
import hr.fer.zemris.optjava.simulatedAnnealing.tempSchedule.ITempSchedule;

import java.util.Random;

class AlgorithmsTemplate {
	private final Random rand;
	
	private final int dim;
	private final double[] maxs;
	private final double[] mins;
	private final double[] deltas;
	
	public AlgorithmsTemplate(Random rand, int dim, double[] maxs, double[] mins, double[] deltas) {
		this.rand = rand;
		this.dim = dim;
		this.maxs = maxs;
		this.mins = mins;
		this.deltas = deltas;
	}
	
	// UTILITY
	private static int[] makeBits(String bitsDefinition) {
		int bits[] = null;
		String[] definitions = bitsDefinition.split(",");
		if(definitions.length > 1) {
			bits = new int[definitions.length];
			for(int i = 0; i < bits.length; ++i) {
				bits[i] = Integer.parseInt(definitions[i]);
			}
		}
		return bits;
	}
	
	private static int makeTotalBits(String bitsDefinition) {
		int total = 0;
		String[] definitions = bitsDefinition.split(",");
		if(definitions.length == 1) {
			total = Integer.parseInt(bitsDefinition);
		} else {
			for(String definition : definitions) {
				total += Integer.parseInt(definition);
			}
		}
		return total;
	}
	
	public double[] makeStartWIth(String startWithDefinition) {
		double[] startingPoint = null;
		String[] definitions = startWithDefinition.split(",");
		if(definitions.length > 0) {
		startingPoint = new double[definitions.length];
			for(int i = 0; i < startingPoint.length; ++i) {
				startingPoint[i] = Double.parseDouble(startWithDefinition);
			}
		}
		return startingPoint;
	}
	
	// DECODER
	private IDecoder<BitVectorSolution> makeBitVectorSolutionDecoder(String decoderType, String bitsDefinition, int n) {
		int[] bits = makeBits(bitsDefinition);
		switch(decoderType) {
		case "binary": 
			if(null == bits) {
				return new NaturalBinaryDecoder(mins, maxs, dim, Integer.parseInt(bitsDefinition));
			} else {
				return new NaturalBinaryDecoder(mins, maxs, bits, dim);
			}
		case "gray": 
			if(null == bits) {
				return new GrayBinaryDecoder(mins, maxs, dim, Integer.parseInt(bitsDefinition));
			} else {
				return new GrayBinaryDecoder(mins, maxs, bits, dim);
			}
		} 
		throw new RuntimeException("No such binary decoder: " + decoderType);
	}
	
	private IDecoder<DoubleArraySolution> makeDoubleArraySolution() {
		return new PassThroughDecoder(mins, maxs);
	}
	
	// NEIGHBORHOOD
	private INeighborhood<BitVectorSolution> makeBitVectorSolutionNeighborhood() {
		return new BitVectorByOneNeighborhood(rand);
	}
	
	private INeighborhood<DoubleArraySolution> makeDoubleArraySolutionNeighborhood(String neighborhoodType) {
		switch(neighborhoodType) {
		case "norm": return new DoubleArrayNormNeighborhood(rand, deltas);
		case "unif": return new DoubleArrayUnifNeighborhood(rand, deltas);
		}
		throw new RuntimeException("No such double array solution neighborhood as: " + neighborhoodType);
	}

	// STARTS WITH	
	private BitVectorSolution makeBitVectorSolutionStartWith(String bitsDefinition, String startWithDefinition) {
		BitVectorSolution solution = new BitVectorSolution(makeTotalBits(bitsDefinition));
		/*if(null == startWithDefinition) {
			solution.randomize(rand);
		} else {
			
		}*/
		solution.randomize(rand);
		return solution;
	}
	
	private DoubleArraySolution makeDoubleArraySolutionStartWith(String startWithDefinition) {
		DoubleArraySolution solution = new DoubleArraySolution(dim);
		/*if(null == startWithDefinition) {
			solution.randomize(rand, maxs, mins);
		} else {
			
		}*/
		solution.randomize(rand, maxs, mins);
		return solution;
	}
	
	// MINIMIZE
	private boolean makeMinimize(String minimize) {
		return Boolean.parseBoolean(minimize);
	}
	
	// MAX ITER
	private int makeMaxIterations(String maxIterations) {
		return Integer.parseInt(maxIterations);
	}
	
	// SCHEDULE
	private ITempSchedule makeTempSchedule(String innerIterations, String outerIterations, String startTemp, String endTemp) {
		int inner = Integer.parseInt(innerIterations);
		int outer = Integer.parseInt(outerIterations);
		double start = Double.parseDouble(startTemp);
		double end = Double.parseDouble(endTemp);
		
		double alpha = GeometricTempSchedule.calculateAlpha(outer, start, end);
		ITempSchedule schedule = new GeometricTempSchedule(alpha, start, inner, outer);
	
		return schedule;
	}
	
	//~~
	public BitVectorSolution runGreedyBinary(Ifunction function, String bits, String decoderType, String startWithDefinition, String maxIterationsDefinition, String minimizeDefinition) {
		IDecoder<BitVectorSolution> decoder = makeBitVectorSolutionDecoder(decoderType, bits, dim);
		INeighborhood<BitVectorSolution> neighborhood = makeBitVectorSolutionNeighborhood();
		BitVectorSolution startWith = makeBitVectorSolutionStartWith(bits, startWithDefinition);
		int maxIterations = makeMaxIterations(maxIterationsDefinition);
		boolean minimize = makeMinimize(minimizeDefinition);
		IOptAlgorithm<BitVectorSolution> optAlgorithm = new GreedyAlgorithm<BitVectorSolution>(decoder, neighborhood, startWith, function, maxIterations, minimize);

		return optAlgorithm.run();
	}
	
	public DoubleArraySolution runGreedyDouble(Ifunction function, String neighborhoodType, String startWithDefinition, String maxIterationsDefinition, String minimizeDefinition) {
		IDecoder<DoubleArraySolution> decoder = makeDoubleArraySolution();
		INeighborhood<DoubleArraySolution> neighborhood = makeDoubleArraySolutionNeighborhood(neighborhoodType);
		DoubleArraySolution startWith = makeDoubleArraySolutionStartWith(startWithDefinition);
		int maxIterations = makeMaxIterations(maxIterationsDefinition);
		boolean minimize = makeMinimize(minimizeDefinition);
		IOptAlgorithm<DoubleArraySolution> optAlgorithm = new GreedyAlgorithm<DoubleArraySolution>(decoder, neighborhood, startWith, function, maxIterations, minimize);
		
		return optAlgorithm.run();
	}
	
	public BitVectorSolution runSimulatedAnnealingBinary(Ifunction function, String bits, String decoderType, String startWithDefinition, String innerIterations, String outerIterations, String startTemp, String endTemp, String minimizeDefinition) {
		IDecoder<BitVectorSolution> decoder = makeBitVectorSolutionDecoder(decoderType, bits, dim);
		INeighborhood<BitVectorSolution> neighborhood = makeBitVectorSolutionNeighborhood();
		BitVectorSolution startWith = makeBitVectorSolutionStartWith(bits, startWithDefinition);
		ITempSchedule tempSchedule = makeTempSchedule(innerIterations, outerIterations, startTemp, endTemp);
		boolean minimize = makeMinimize(minimizeDefinition);
		IOptAlgorithm<BitVectorSolution> algorithm = new SimulatedAnnealing<BitVectorSolution>(rand, decoder, neighborhood, startWith, function, tempSchedule, minimize);
		
		return algorithm.run();
	}
	
	public DoubleArraySolution runSimulatedAnnealingDouble(Ifunction function, String neighborhoodType, String startWithDefinition, String innerIterations, String outerIterations, String startTemp, String endTemp, String minimizeDefinition) {
		IDecoder<DoubleArraySolution> decoder = makeDoubleArraySolution();
		INeighborhood<DoubleArraySolution> neighborhood = makeDoubleArraySolutionNeighborhood(neighborhoodType);
		DoubleArraySolution startWith = makeDoubleArraySolutionStartWith(startWithDefinition);
		ITempSchedule tempSchedule = makeTempSchedule(innerIterations, outerIterations, startTemp, endTemp);
		boolean minimize = makeMinimize(minimizeDefinition);
		IOptAlgorithm<DoubleArraySolution> algorithm = new SimulatedAnnealing<DoubleArraySolution>(rand, decoder, neighborhood, startWith, function, tempSchedule, minimize);
		
		return algorithm.run();
	}
}
