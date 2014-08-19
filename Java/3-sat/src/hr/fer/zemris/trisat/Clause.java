package hr.fer.zemris.trisat;

public class Clause {
	protected int indexesField[];
	
	public Clause(int[] indexes) {
		indexesField = new int[indexes.length];
		System.arraycopy(indexes, 0, indexesField, 0, indexes.length);
	}
	
	public int getSize() {
		return indexesField.length;
	}
	
	public int getLiteral(int index) {
		return indexesField[index];
	}
	
	public boolean isSatisfied(BitVector assigment) {
		for(int i = 0; i < indexesField.length; ++i) {
			int convertedToBitVectorIndex = indexesField[i];
			if(convertedToBitVectorIndex < 0) {
				convertedToBitVectorIndex = -convertedToBitVectorIndex - 1;
				if(false == assigment.get(convertedToBitVectorIndex)) {
					return true;
				}
			} else {
				--convertedToBitVectorIndex;
				if(true == assigment.get(convertedToBitVectorIndex)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	@Override
	public String toString() {
		StringBuilder ss = new StringBuilder();
		for(int index : indexesField) {
			if(index < 0) {
				ss.append('-').append('x').append(Integer.toString(-index));
			} else {
				ss.append('+').append('x').append(Integer.toString(index));
			}
		}
		ss.deleteCharAt(0);
		return ss.toString();
	}
}
