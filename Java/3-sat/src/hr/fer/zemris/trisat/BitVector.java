package hr.fer.zemris.trisat;

import java.util.Random;

public class BitVector implements Comparable<MutableBitVector> {
	protected boolean[] bitField;
	
	@Override
	public int compareTo(MutableBitVector other) {
		for(int i = 0; i < bitField.length; ++i) {
			boolean thisBit = bitField[i];
			boolean thatBit = other.bitField[i]; 
			if(thisBit == thatBit) {
				continue;
			} else if (thisBit) {
				return 1;
			} else {
				return -1;
			}
		}
		
		return 0;
	}
	
	public BitVector(int n) {
		bitField = new boolean[n];
	}
	
	public BitVector(boolean ... bits) {
		this(bits.length);	
		System.arraycopy(bits, 0, bitField, 0, bits.length);
	}
	
	public BitVector(Random rand, int numberOfBits) {
		this(numberOfBits);	
		for(int i = 0; i < bitField.length; ++i) {
			bitField[i] = rand.nextBoolean();
		}
	}
	

	public boolean get(int index) {
		return bitField[index];
	}
	
	public int getSize() {
		return bitField.length;
	}
	
	@Override
	public String toString() {
		StringBuilder ss = new StringBuilder();
		for(int i = 0; i < bitField.length; ++i) {
			Boolean bit = bitField[i];
			if(bit) {
				ss.append('1');
			} else {
				ss.append('0');
			}
		}
		return ss.toString();
	}
	
	public MutableBitVector copy() {
		return new MutableBitVector(bitField);
	}
	
}
