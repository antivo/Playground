package hr.fer.zemris.optjava.dz3;

public abstract class BitVectorDecoder implements IDecoder<BitVectorSolution> {
	protected double mins[];
	protected double maxs[];
	protected int bits[]; 
	protected int n;
	protected int totalBits;
	
	/*public BitVectorDecoder(double mins[], double maxs[], int bits[], int) {
		// TODO Auto-generated constructor stub
	}*/
	
	/*public BitVectorDecoder(double, double, int, int) {
		// TODO Auto-generated constructor stub
	}*/
	
	public int getTotalBits() {
		return totalBits;
	}
	
	public int getDimensions() {
		return n;
	}
}
