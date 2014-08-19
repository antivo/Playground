package hr.fer.zemris.fenc.element;

import hr.fer.zemris.fenc.domain.DeclarationException;

public class IntegerElement {
	static public long getElemFromString(String elem) throws DeclarationException {
		try {
			return java.lang.Long.parseLong(elem);
		} catch(NumberFormatException e) {
			throw new DeclarationException("Integer element must be integer number");
		}
		finally {
			
		}
	}
}
