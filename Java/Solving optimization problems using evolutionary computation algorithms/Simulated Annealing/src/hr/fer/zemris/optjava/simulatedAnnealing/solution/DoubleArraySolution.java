package hr.fer.zemris.optjava.simulatedAnnealing.solution;
import java.util.Random;

public class DoubleArraySolution extends SingleObjectiveSolution {
	public double[] values;
	
	public DoubleArraySolution(int size) {
		values = new double[size];
	}
	
	@Override
	public DoubleArraySolution duplicate() {
		DoubleArraySolution solution = new DoubleArraySolution(values.length);
		for(int i = 0; i < values.length; ++i) {
			solution.values[i] = values[i];
		}
		
		return solution;
	}
	
	public void randomize(Random rand, double[] maxs, double mins[]) {
		for(int i = 0; i < values.length; ++i) {
			double interval = maxs[i] - mins[i];
			values[i] = mins[i] + rand.nextDouble() * interval;
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString()).append(" [");
		for(int i = 0; i < values.length - 1; ++i) {
			sb.append(format(values[i])).append(", ");
		}
		sb.append(format(values[values.length - 1])).append("]");
		return sb.toString();
	}
}
