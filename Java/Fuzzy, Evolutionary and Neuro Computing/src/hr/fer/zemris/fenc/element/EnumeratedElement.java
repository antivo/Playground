package hr.fer.zemris.fenc.element;

import hr.fer.zemris.fenc.domain.DeclarationException;

public class EnumeratedElement {
	static public String getElemFromString(String elem) throws DeclarationException {
		String pattern = "^[a-zA-Z0-9]*$";
		if(!elem.matches(pattern)) {
			throw new DeclarationException("Enumerated elements can only contain alphanumeric characters");
		}
		
		return elem;
	}
}
