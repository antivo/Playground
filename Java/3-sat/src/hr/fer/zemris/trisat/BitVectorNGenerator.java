package hr.fer.zemris.trisat;

import java.util.Iterator;

public class BitVectorNGenerator implements Iterable<MutableBitVector>{
	BitVector assigment;
	
	public BitVectorNGenerator(BitVector assigment) {
		this.assigment = assigment;
	}
	
	protected MutableBitVector createMutatedBitVector(int position) {
		MutableBitVector mutableBitVector = assigment.copy();
		boolean invertedBit = !assigment.get(position);
		mutableBitVector.set(position, invertedBit);
		return mutableBitVector;
	}
	
	@Override
	public Iterator<MutableBitVector> iterator() {
		return new Iterator<MutableBitVector>() {
			private int pos = 0;
			private final int length = assigment.getSize();
			
			@Override
			public boolean hasNext() {
				if(pos < length) {
					return true;
				}
				return false;
			}

			@Override
			public MutableBitVector next() {
				MutableBitVector nextNeighbour = createMutatedBitVector(pos);
				++pos;
				return nextNeighbour;
			}

			@Override
			public void remove() {
				// TODO Auto-generated method stub	
			}
		
		};
	}
	
	public MutableBitVector[] createNeighbourhood() {
		MutableBitVector[] mutableBitVectorArray = new MutableBitVector[assigment.getSize()];
		for(int i = 0; i < mutableBitVectorArray.length; ++i) {
			mutableBitVectorArray[i] = createMutatedBitVector(i);
		}
		return mutableBitVectorArray;
	}

}
