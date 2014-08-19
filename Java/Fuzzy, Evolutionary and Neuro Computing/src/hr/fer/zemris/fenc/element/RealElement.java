package hr.fer.zemris.fenc.element;

import hr.fer.zemris.fenc.domain.DeclarationException;

public class RealElement {
	public static double getElemFromString(String elem) throws DeclarationException {
		try {
			return java.lang.Double.parseDouble(elem);
		} catch(NumberFormatException e) {
			throw new DeclarationException("Real element must be real number");
		}
	}
}
