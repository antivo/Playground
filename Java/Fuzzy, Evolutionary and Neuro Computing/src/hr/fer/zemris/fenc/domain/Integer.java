package hr.fer.zemris.fenc.domain;

import hr.fer.zemris.fenc.element.IntegerElement;

public class Integer implements Domain {
	private final long from;
	private final long to;
	private final long step;
	private final int cardinality;
	
	private void assertArguments() throws DeclarationException {
		if(0 == step && from != to) {
			throw new DeclarationException("STEP can not be zero unless FROM is equal to TO");
		}
		
		if(from > to) {
			throw new DeclarationException("FROM must be equal or lesser than TO");
		}
		
		if(step < 0) {
			throw new DeclarationException("STEP must be equal or larger to 0");
		}
	}
	
	public Integer(long from, long to, long step) throws DeclarationException {
		assertArguments();
		
		long k = to - from;
		if(0 == k) {
			step = 0;
		} else {
			k /= step;
		}
		
		this.from = from;
		this.step = step;
		this.to = this.from + this.step * k;
		this.cardinality = (int) (k + 1);
	}
	
	@Override
	public boolean equals(Domain other) {
		if(other instanceof Integer) {
			Integer rhs = (Integer) other;
			if(from == rhs.from && to == rhs.to && step == rhs.step) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean hasElement(String elem) throws DeclarationException {
		final long element = IntegerElement.getElemFromString(elem);
		
		if(element >= from && element <= to) {
			if(element == from || (element - from) % step == 0) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public int getCardinality() {
		return cardinality;
	}

	@Override
	public String[] getDomainComponents() {
		String[] result = null;
		result = new String[cardinality];
		for(int i = 0; i < cardinality; ++i) {
			result[i] = Long.toString(from + i*step);
		}
		
		return result;
	}

	@Override
	public int getIndexOfElement(String value) throws DeclarationException, DomainException {
		if(!hasElement(value)) {
			throw new DomainException();
		}

		int elem = java.lang.Integer.parseInt(value);
		int result = 0;
		if(elem != from) {
			result = (int) ((elem - from) / step);
		}
		
		return result;
	}

}
