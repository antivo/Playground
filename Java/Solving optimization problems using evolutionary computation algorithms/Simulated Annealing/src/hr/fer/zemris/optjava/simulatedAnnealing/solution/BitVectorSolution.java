package hr.fer.zemris.optjava.simulatedAnnealing.solution;
import java.util.Random;


public class BitVectorSolution extends SingleObjectiveSolution {
	public boolean[] bits;

	public BitVectorSolution(int size) {
		bits = new boolean[size];
	}
	
	@Override
	public BitVectorSolution duplicate() {
		BitVectorSolution solution = new BitVectorSolution(bits.length);
		for(int i = 0; i < bits.length; ++i) {
			solution.bits[i] = bits[i];
		}
		return solution;
	}
	
	public void randomize(Random rand) {
		for(int i = 0; i < bits.length; ++i) {
			bits[i] = rand.nextBoolean();
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString()).append(" [");
		
		for(int i = 0; i < bits.length; ++i) {
			char c = bits[i] ? '1' : '0'; 
			sb.append(c);
		}
		sb.append("]");
		return sb.toString();
	}
}
