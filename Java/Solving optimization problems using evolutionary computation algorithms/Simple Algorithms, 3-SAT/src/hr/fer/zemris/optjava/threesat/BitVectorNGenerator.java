package hr.fer.zemris.optjava.threesat;

import java.util.Iterator;

public class BitVectorNGenerator implements Iterable<MutableBitVector>{
	BitVector bitVector;
	
	public BitVectorNGenerator(BitVector assigment) {
		bitVector = assigment;
	}

	@Override
	public Iterator<MutableBitVector> iterator() {
		return new Iterator<MutableBitVector>() {
			int pos = 1;
			
			@Override
			public MutableBitVector next() {
				MutableBitVector next = bitVector.copy();
				boolean value = next.get(pos);
				value = !value;
				next.set(pos, value);
				++pos;
				return next;
			}
			
			@Override
			public boolean hasNext() {
				return pos <= bitVector.getSize();
			}
		};
	}
	
	public MutableBitVector[] craeteNeighborhood() {
		int length = bitVector.getSize(); 
		MutableBitVector[] neighborhood = new MutableBitVector[length];
		for(int i = 1; i <= length; ++i) {
			boolean value = bitVector.get(i);
			value = !value;
			
			MutableBitVector next = bitVector.copy();
			next.set(i, value);
			neighborhood[i - 1] = next;
		}
		return neighborhood;
	}
}
