package hr.fer.zemris.optjava.simulatedAnnealing.decoder;


public class GrayBinaryDecoder extends BitVectorDecoder  {
	public GrayBinaryDecoder(double[] mins, double[] maxs, int[] bits, int n) {
		super(mins, maxs, bits, n);
	}
	
	public GrayBinaryDecoder(double[] mins, double[] maxs, int n, int totalBits) {
		super(mins, maxs, n, totalBits);
	}

	public static boolean[] grayToBinary(boolean[] binary) {
		boolean[] gray = new boolean[binary.length];
		if(binary.length > 0) {
			gray[0] = binary[0];
			for(int i = 1; i < binary.length; ++i) {
				gray[i] = binary[i] ^ gray[i - 1];
			}
		}
		return gray;
	}

	@Override
	protected boolean[] codeToBinary(boolean[] code) {
		return grayToBinary(code);
	}
}
