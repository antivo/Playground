package hr.fer.zemris.fenc.domain;

import hr.fer.zemris.fenc.element.EnumeratedElement;

import java.util.HashSet;
import java.util.Set;

public class Enumerated implements Domain {
	private final String[] elems; // late fix up
	private final Set<String> elements;

	public Enumerated(String... args) throws DeclarationException {
		elements = new HashSet<String>();
		elems = new String[args.length];
		int idx = 0;
		for(String elem : args) {
			if(elements.contains(elem)) {
				throw new DeclarationException("Elements can not repeat in Enumerated set");
			}
			elements.add(elem);
			elems[idx++] = elem;
		}
	}
	
	@Override
	public boolean equals(Domain other) {
		if(other instanceof Enumerated) {
			Enumerated rhs = (Enumerated) other;
			return elements.equals(rhs.elements);
		}
		return false;
	}

	@Override
	public boolean hasElement(String elem) throws DeclarationException {
		String e = EnumeratedElement.getElemFromString(elem);
		return elements.contains(e);
	}

	@Override
	public int getCardinality() {
		return elements.size();
	}

	@Override
	public String[] getDomainComponents() {
		//return (String[]) elements.toArray(new String[0]);
		return elems;
	}

	@Override
	public int getIndexOfElement(String value) throws DeclarationException, DomainException {
		int ret = -1;
		for(int i = 0; i < elems.length; ++i) {
			if(elems[i].equals(value)) {
				ret = i;
				break;
			}
		}
		
		if(ret == -1) {
			throw new DomainException();
		}
		
		return ret;
	}
}
