package hr.fer.zemris.fenc.domain;

import hr.fer.zemris.fenc.element.CartesianElement;

import java.util.Vector;


public class Cartesian implements Domain {
	private final Vector<Domain> elements;

	private void assertArguments(Domain... domains) throws DeclarationException {
		if(domains.length < 2) {
			throw new DeclarationException("Cartesian set must involve at least two DOMAINS");
		}
	}
	
	public Cartesian(Domain... domains) throws DeclarationException {
		assertArguments(domains);
		
		elements = new Vector<Domain>(domains.length);
		for(Domain d : domains) {
			elements.add(d);
		}
	}
	
	@Override
	public boolean equals(Domain other) {
		if(other instanceof Cartesian) {
			Cartesian rhs = (Cartesian) other;
			
			if(rhs.elements.size() == elements.size()) {
				for(int i = 0; i < elements.size(); ++i) {
					Domain a = elements.get(i);
					Domain b = rhs.elements.get(i);
					if(!a.equals(b)) {
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean hasElement(String elem) throws DeclarationException {
		String[] elems = CartesianElement.getElemsFromProduct(elem);
		if(elems.length == elements.size()) {
			for(int i = 0; i < elems.length; ++i) {
				String e = elems[i];
				if(!elements.get(i).hasElement(e)) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public int getCardinality() {
		int cardinality = 1;
		for(Domain domain : elements) {
			cardinality *= domain.getCardinality();
		}
		return cardinality;
	}

	@Override
	public String[] getDomainComponents() {
		int cardinality = getCardinality();
		StringBuilder[] cartesian = new StringBuilder[cardinality];
		for(int i = 0; i < cardinality; ++i) {
			cartesian[i] = new StringBuilder();
		}
		
		boolean first = true;
		int previousDomaimCardinality = 1;
		for(Domain d : elements) {
			String[] xd = d.getDomainComponents();
			int thisDomainCardinality = xd.length;
			int idx = 0;
			
			if(first) {
				first = false;
				
				for(int i = 0; i < cardinality; ++i) {
					cartesian[i].append(xd[idx % thisDomainCardinality]);
					++idx;
				}
			} else {
				int round = 0;
				
				for(int i = 0; i < cardinality; ++i) {
					cartesian[i].append(',').append(xd[idx % thisDomainCardinality]);
					++round;
					if(round >= previousDomaimCardinality) {
						++idx;
						round = 0;
					}
				}
			}
			
			previousDomaimCardinality *= thisDomainCardinality;
		}
		
		String[] result = new String[cardinality];
		for(int i = 0; i < cardinality; ++i) {
			result[i] = cartesian[i].toString();
		}
		
		return result;		
	}

	@Override
	public int getIndexOfElement(String value) throws DeclarationException, DomainException {
		if(!hasElement(value)) {
			throw new DomainException();
		}

		// TODO speedup
		
		int result = -1;
		String[] elems = getDomainComponents();
		for(int i = 0; i < elems.length; ++i) {
			if(elems[i].equals(value)) {
				result = i;
				break;
			}
		}
		return result;
	}

	public boolean isRelation() {
		Domain d = elements.firstElement();
		for(int i = 1; i < elements.size(); ++i) {
			if(!elements.get(i).equals(d)) {
				return false;
			}
		}
		return true;
	}
	
	public int realationOver() {
		
		return elements.size();
	}
	
	public Domain getSingular() {
		return elements.firstElement();
	}

	public Domain[] getDomains() {
		Domain[] allDomains = new Domain[elements.size()];
		for(int i = 0; i < allDomains.length; ++i) {
			allDomains[i] = elements.get(i);
		}
		return allDomains;
	}
	
	public Domain getExcept(Domain... domains) throws DeclarationException {
		if(domains.length >= elements.size()) {
			return null;
		}
		
		Domain[] allDomains = new Domain[elements.size()];
		for(int i = 0; i < allDomains.length; ++i) {
			allDomains[i] = elements.get(i);
		}
		
		
		for(int i = 0; i < allDomains.length; ++i) {
			for(int j = 0; j < domains.length; ++j) {
				if(allDomains[i].equals(domains[j])) {
					allDomains[i] = null;
					domains[j] = null;
				}
			}
		}
		
		for(Domain test : domains) {
			if(test != null) {
				return null;
			}
		}
		
		Domain[] result = new Domain[elements.size() - domains.length];
		int idxResult = 0;
		for(int i = 0; idxResult < allDomains.length; ++i) {
			if(allDomains[i] != null) {
				result[idxResult] = allDomains[i];
				++idxResult;
			}
		}
		
		if(result.length > 1) {
			return new Cartesian(result);
		} else {
			return result[0];
		}
	}
}
