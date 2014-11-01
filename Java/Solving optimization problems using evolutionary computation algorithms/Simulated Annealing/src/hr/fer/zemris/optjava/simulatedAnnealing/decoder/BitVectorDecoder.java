package hr.fer.zemris.optjava.simulatedAnnealing.decoder;

import hr.fer.zemris.optjava.simulatedAnnealing.solution.BitVectorSolution;

public abstract class BitVectorDecoder implements IDecoder<BitVectorSolution> {
	protected final double[] mins;
	protected final double[] maxs;
	protected final int[] bits;
	protected final int n;
	protected final int totalBits;

	private static void assertBitstringToDimensionMappint(int[] bits, int n) {
		if(bits.length != n) {
			throw new RuntimeException("Dimension is " + n + ", but there are" + bits.length + " regions for mapping");
		}
	}

	private static void assertInterpolationIndependence(int n, int totalBits) {
		if(totalBits < n) {
			throw new RuntimeException("Dimension is " + n + ", but bitstring length is only " + totalBits);
		}
	}

	public BitVectorDecoder(double[] mins, double[] maxs, int[] bits, int n) {
		assertBitstringToDimensionMappint(bits, n);
		
		this.mins = mins;
		this.maxs = maxs;
		this.bits = bits;
		this.n = n;
		
		int totalBits = 0;
		for(int i = 0; i < bits.length; ++i) {
			totalBits += bits[i];
		}
		this.totalBits = totalBits;
	}

	public BitVectorDecoder(double[] mins, double[] maxs, int n, int totalBits) {
		assertInterpolationIndependence(n, totalBits);
		
		this.mins = mins;
		this.maxs = maxs;
		this.totalBits = totalBits;
		this.n = n;
		this.bits = new int[n];
		
		// initialize this.bits
		final int atLeastAssigned = totalBits / n;
		final int toDivide = totalBits % n;
		final double q = toDivide / (double) n;
		double sum = 0;
		int taken = 0;
		for(int i = 0; i < n - 1; ++i) {
			sum += q;
			int toTake = (int) Math.floor(sum);
			sum -= toTake;
			if(sum < 0) { // just in case of problems with double representation
				sum += toTake;
				toTake = 0;
			}
			
			toTake += atLeastAssigned;
			bits[i] = toTake;
			
			taken += toTake;
		}
		this.bits[n - 1] = totalBits - taken;
	}
	
	public int getTotalBits() {
		return totalBits;
	}
	
	public int getDimensions() {
		return n;
	}
	
	private static boolean[] extractSubCode(boolean[] bits, int from, int length) {
		boolean[] subCode = new boolean[length];
		int to = from + length;
		for(int i = from, idx = 0; i < to; ++i, ++idx) {
			subCode[idx] = bits[i];
		}
		return subCode;
	}
	
	protected abstract boolean[] codeToBinary(boolean[] code);
	
	public double[] decode(BitVectorSolution bitVectorSolution) {
		int dim = getDimensions();
		double[] decoded = new double[dim];
		decode(bitVectorSolution, decoded);
		return decoded;
	}
	
	private long binaryToLong(boolean[] code) {
		int multiplier = 1;
		long result = 0;
		for(int i = code.length - 1; i >= 0; --i) {
			if(code[i]) {
				result += multiplier;
			}
			multiplier *= 2;
		}
		
		return result;
	}
	
	public static double decodeToDouble(long binary, double max, double min, long parts) {
		double interval = max - min;
		double q = interval / (parts - 1);
		double kq = q * binary;
		return min + kq;
	}
	
	public void decode(BitVectorSolution bitVectorSolution, double[] output) {
		int from = 0;
		int length;
		for(int i = 0; i < output.length; ++i) {
			length = bits[i];
			boolean[] subCode = extractSubCode(bitVectorSolution.bits, from, length);
			from += length;
			
			boolean[] binary = codeToBinary(subCode);
			
			output[i] = decodeToDouble(binaryToLong(binary), maxs[i], mins[i], ((long) Math.pow(2, binary.length) - 1));
		}
	}
}
