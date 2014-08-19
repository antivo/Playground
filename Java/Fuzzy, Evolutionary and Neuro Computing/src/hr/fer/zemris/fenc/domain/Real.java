package hr.fer.zemris.fenc.domain;

import hr.fer.zemris.fenc.element.RealElement;

public class Real implements Domain {
	private final double from;
	private final double to;
	private final double step;
	private final int cardinality;
	
	static private final double epsilon = 1.0E-7; 
	
	static private boolean eq(double a, double b) {
		if (Math.abs(a - b) < epsilon) {
			return true;
		}
		return false;
	}
	
	private void assertArguments() throws DeclarationException {
		if(eq(step,0) && !eq(from, to)) {
			throw new DeclarationException("STEP can not be zero unless FROM is equal to TO");
		}
		
		if(from > to) {
			throw new DeclarationException("FROM must be equal or lesser than TO");
		}
		
		if(step < 0) {
			throw new DeclarationException("STEP must be equal or larger to 0");
		}
	}
	
	public Real(double from, double to, double step) throws DeclarationException {
		assertArguments();
		
		double distance = to - from; 
		if(eq(distance, 0)) {
			step = 0;
		} else {
			distance /= step;
		}
		final long k = Math.round(distance - 0.5f);
		
		this.from = from;
		this.step = step;
		this.to = this.from + this.step * k;
		this.cardinality = (int) k + 1;
	}
	
	@Override
	public boolean equals(Domain other) {
		if(other instanceof Real) {
			Real rhs = (Real) other;
			if(eq(from, rhs.from) && eq(to, rhs.to) && eq(step, rhs.step)) {
				return true;
			}	
		}
		return false;
	}

	@Override
	public boolean hasElement(String elem) throws DeclarationException {
		final double element = RealElement.getElemFromString(elem);
		
		if(eq(element,from) || eq(element, to)) {
			return true;
		}
		
		
		if(element > from && element < to) {
			final double steps = (to - from) / step; 
			final long times = Math.round(steps - 0.5f);
			if(eq(steps,times)) {
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
			result[i] = Double.toString(from + i*step);
		}
		
		return result;
	}

	@Override
	public int getIndexOfElement(String value) throws DeclarationException, DomainException {
		if(!hasElement(value)) {
			throw new DomainException();
		}

		double elem = Double.parseDouble(value);
		int result = 0;
		if(elem != from) {
			double times = (elem - from) / step;
			result = (int) Math.round(times);
		}
		
		return result;
	}
}
