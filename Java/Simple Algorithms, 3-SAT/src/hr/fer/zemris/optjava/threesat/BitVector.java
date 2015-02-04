package hr.fer.zemris.optjava.threesat;

import java.util.Random;

public class BitVector implements Comparable<BitVector> {
	protected boolean[] bitVector;
	
	@Override
	public int compareTo(BitVector other) {
		if(null == other || this.bitVector.length != other.bitVector.length) {
			return 1;
		} 
		
		for(int i = 0; i < bitVector.length; ++i) {
			boolean thisBit = bitVector[i];
			boolean thatBit = other.bitVector[i]; 
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
	
	public BitVector(Random rand, int numberOfBits) {
		bitVector = new boolean[numberOfBits];
		for(int i = 0; i < numberOfBits; ++i) {
			bitVector[i] = rand.nextBoolean();
		}
	}
	
	public BitVector(boolean... bits) {
		bitVector = bits.clone();
	}
	
	public BitVector(int n) {
		bitVector = new boolean[n];
		for(int i = 0; i < n; ++i) {
			bitVector[i] = false;
		}
	}
	
	public boolean get(int index) {
		return bitVector[index - 1];
	}
	
	public int getSize() {
		return bitVector.length;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < bitVector.length; ++i) {
			if(bitVector[i]) {
				sb.append('1');
			} else {
				sb.append('0');
			}
;		}
		return sb.toString();
	}
	
	public MutableBitVector copy() {
		return new MutableBitVector(this.bitVector);
	}
}
