package hr.fer.zemris.nenr.fec.ds;

import java.util.List;

public class Dataset {
	private final int size;
	private final List<double[]> input;
	private final List<double[]> output;
	
	
	
	public List<double[]> getInput() {
		return input;
	}

	public List<double[]> getOutput() {
		return output;
	}

	private static void assertArguments(List<double[]> input, List<double[]> output) throws IllegalArgumentException {
		if(input.size() != output.size()) {
			throw new IllegalArgumentException("Examples in dataset must have equal number of inputs and outputs");
		}
	}
	
	public Dataset(List<double[]> input, List<double[]> output) {
		assertArguments(input, input);
		
		this.input = input;
		this.output = output;
		this.size = input.size();
	}
	
	public int getSize() {
		return size;
	}
	
	public double[] getInput(int idx) {
		return input.get(idx);
	}
	
	public double[] getOutput(int idx) {
		return output.get(idx);
	}
	
	public void printAll() {
		System.out.println("DATASET");
		
		for(int i = 0; i < size; ++i) {
			double[] in  = input.get(i);
			StringBuilder sb = new StringBuilder();
			for(int j = 0; j < in.length; ++j) {
				sb.append(in[j]).append(" ");
			}
			
			double[] out = output.get(i);
			for(int j = 0; j < out.length; ++j) {
				sb.append((new Double(out[j])).intValue()).append(" ");
			}
			
			System.out.println(sb.toString());
		}
	}
}
