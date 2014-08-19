package hr.fer.zemirs.fenc.set;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import hr.fer.zemris.fenc.domain.DeclarationException;
import hr.fer.zemris.fenc.domain.Domain;
import hr.fer.zemris.fenc.domain.DomainException;

public class FuzzySet {
	protected final Domain domain;
	protected final Map<String, Double> set;
	
	public void assertElement(String elem) throws DeclarationException, DomainException {
		if(!domain.hasElement(elem)) {
			throw new DomainException();
		}
	}

	public FuzzySet(Domain domain, Map<String, Double> set) throws DeclarationException {
		Iterator<Map.Entry<String,Double>> iter = set.entrySet().iterator();
		while (iter.hasNext()) {
		    Map.Entry<String, Double> entry = iter.next();
		    if(0 == entry.getValue()) {
		        iter.remove();
		    }
		}
		
		this.domain = domain;
		this.set = set;
	}
	
	public Domain getDomain() {
		return domain;
	}
	
	public double getMembershipFor(String elem) throws DeclarationException, DomainException {
		assertElement(elem);
		double result = 0;
		if (set.containsKey(elem)) {
			result = set.get(elem);
		}
		
		return result;
	}
	
	
	public FuzzySet cut(double a) throws DeclarationException {
		Map<String, Double> newSet = new HashMap<String, Double>();
		for(Entry<String, Double> elem : this.set.entrySet()) {
			String key = elem.getKey();
			double val = elem.getValue();
			
			newSet.put(key, Math.min(a, val));
		}
		
		return new FuzzySet(domain, newSet);
	}
	
	public FuzzySet scale(double a) throws DeclarationException {
		Map<String, Double> newSet = new HashMap<String, Double>();
		for(Entry<String, Double> elem : this.set.entrySet()) {
			String key = elem.getKey();
			double val = elem.getValue();
			
			newSet.put(key, a * val);
		}
		
		return new FuzzySet(domain, newSet);
	}
}
