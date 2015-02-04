package hr.fer.zemirs.fenc.set.builder;

import hr.fer.zemirs.fenc.set.FuzzySet;
import hr.fer.zemris.fenc.domain.DeclarationException;
import hr.fer.zemris.fenc.domain.Domain;

import java.util.HashMap;
import java.util.Map;

abstract public class FuzzySetBuilder {
	public static FuzzySet composeFuzzySet(Domain domain, Map<String, Double> set) throws DeclarationException {
		for(Map.Entry<String, Double> pair : set.entrySet()) {
			String elem = pair.getKey();
			
			if(!domain.hasElement(elem)) {
				throw new DeclarationException("Set elements do not match it's domain");
			}
		}
		
		return createFuzzySet(domain, set);
	}
	
	protected static FuzzySet createFuzzySet(Domain domain, Map<String, Double> set) throws DeclarationException {
		return new FuzzySet(domain, set);
	}
	
	protected static Map<String, Double> addToSet(Map<String, Double> set, String elem, Double value) throws DeclarationException {
		if(null != set.put(elem, value)) {
			throw new DeclarationException("Element can not be repeated in set delcaration");
		}
		return set;
	}
	
	protected static Map<String, Double> getInitialFuzzySetContainer() {
		return new HashMap<String, Double>();
	}
}
