package hr.fer.zemris.optjava.threesat;

public class Clause {
	int[] indexes;
	
	public Clause(int[] indexes) {
		this.indexes = indexes.clone();
	}
	
	public int getSize() {
		return indexes.length;
	}
	
	public int getLiteral(int index) {
		return indexes[index];
	}
	
	public boolean isSatisfied(BitVector assigment) {
		boolean satisfied = false;
		for(int i = 0; i < indexes.length; ++i) {
			int index = indexes[i];
			
			boolean negative = false;
			if(index < 0) {
				negative = true;
				index = -index;
			}
			
			boolean literal = assigment.get(index);
			if(negative) {
				literal = !literal;
			}
			
			if(literal) {
				satisfied = true;
				break;
			}
		}
		return satisfied;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < indexes.length; ++i) {
			if(i > 0) {
				sb.append(' ');
			}
			sb.append(indexes[i]);
		}
		return sb.toString();
	}
}
