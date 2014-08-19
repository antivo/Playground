package hr.fer.zemris.trisat;

public class MutableBitVector extends BitVector {
	MutableBitVector(boolean... bits) {
		super(bits);
	}
	
	MutableBitVector(int n) {
		super(n);
	}
	
	public void set(int index, boolean value) {
		this.bitField[index] = value;
	}
}
