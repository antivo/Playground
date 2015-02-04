package hr.fer.zemris.optjava.simulatedAnnealing.decoder;


public class NaturalBinaryDecoder extends BitVectorDecoder {
	public NaturalBinaryDecoder(double[] mins, double[] maxs, int[] bits, int n) { 
		super(mins, maxs, bits, n);
	}
	
	public NaturalBinaryDecoder(double[] mins, double[] maxs, int n, int totalBits) {
		super(mins, maxs, n, totalBits);
	}

	@Override
	protected boolean[] codeToBinary(boolean[] code) {
		return code;
	}
	
}
