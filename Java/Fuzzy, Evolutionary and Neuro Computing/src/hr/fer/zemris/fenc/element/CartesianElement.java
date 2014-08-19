package hr.fer.zemris.fenc.element;

import hr.fer.zemris.fenc.domain.DeclarationException;

public class CartesianElement {
	static final private String delimiter = ",";
	
	public static String[] getElemsFromProduct(String elem) throws DeclarationException {
		String[] e = elem.split(delimiter);
		if(e.length < 2) {
			throw new DeclarationException("Cartesian element must be composed from at least two elements");
		}
		return e;
	}
}
