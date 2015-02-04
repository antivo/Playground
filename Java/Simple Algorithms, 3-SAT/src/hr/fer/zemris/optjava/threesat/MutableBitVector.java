package hr.fer.zemris.optjava.threesat;

public class MutableBitVector extends BitVector {
	public MutableBitVector(boolean... bits) {
		super(bits);
	}
	
	public MutableBitVector(int n) {
		super(n);
	}
	
	public void set(int index, boolean value) {
		this.bitVector[index - 1] = value;
	}
}
