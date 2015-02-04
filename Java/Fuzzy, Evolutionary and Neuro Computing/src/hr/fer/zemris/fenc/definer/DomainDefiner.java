package hr.fer.zemris.fenc.definer;

import hr.fer.zemirs.fenc.container.EntityContainer;
import hr.fer.zemirs.fenc.container.UndefinedEntity;
import hr.fer.zemris.fenc.domain.Cartesian;
import hr.fer.zemris.fenc.domain.DeclarationException;
import hr.fer.zemris.fenc.domain.Domain;
import hr.fer.zemris.fenc.domain.Enumerated;
import hr.fer.zemris.fenc.domain.Integer;
import hr.fer.zemris.fenc.domain.Real;

// enumerated {x1, x2, x3, x4}
// integer -2 to 10 step 1
// domain real -5 to 5 step 0.1
// domain cartesian d1,d2,d3
public class DomainDefiner extends Definer {
	public static Domain defineDomain(String domainDefinition, EntityContainer<Domain> domainContainer) throws DeclarationException, UndefinedEntity {
		int firstWhitespace = domainDefinition.indexOf(' ');
		String domainType = domainDefinition.substring(0, firstWhitespace).trim();
		String definition = domainDefinition.substring(firstWhitespace + 1).trim();
		
		switch(domainType) {
		case "enumerated": return makeEnumerated(definition);
		case "integer": return makeInteger(definition);
		case "real": return makeReal(definition);
		case "cartesian": return makeCartesian(definition, domainContainer);
		
		default: throw new DeclarationException("Unsuported type of domain");
		}
	}
	
	static private Enumerated makeEnumerated(String domainDefinition) throws DeclarationException {
		if('{' == domainDefinition.charAt(0)) {
			int lastChar = domainDefinition.length() - 1;
			if('}' == domainDefinition.charAt(lastChar)) {
				String elementsDefinition = domainDefinition.substring(1, lastChar);
				
				String[] elements = elementsDefinition.split(",");
				elements = trim(elements);
				
				return new Enumerated(elements);
			}
		}
		
		throw new DeclarationException("Illegal declaration of Enumerated Domain");
	}
	
	static private Integer makeInteger(String domainDefinition) throws DeclarationException {
		try {
			String[] defs = domainDefinition.split("\\s+");
			if(5 == defs.length) {
				if(defs[1].equals("to")) {
					if(defs[3].equalsIgnoreCase("step")) {
						int from = java.lang.Integer.parseInt(defs[0]);
						int to = java.lang.Integer.parseInt(defs[2]);
						int step = java.lang.Integer.parseInt(defs[4]);
						
						return new Integer(from, to, step);
					}
				}
			}
		} catch(Exception e) {
			// NOTHING
		}
		
		throw new DeclarationException("Illegal declaration of Integer Domain");
	}
	
	static private Real makeReal(String domainDefinition) throws DeclarationException {
		try {
			String[] defs = domainDefinition.split("\\s+");
			if(5 == defs.length) {
				if(defs[1].equals("to")) {
					if(defs[3].equalsIgnoreCase("step")) {
						double from = Double.parseDouble(defs[0]);
						double to = Double.parseDouble(defs[2]);
						double step = Double.parseDouble(defs[4]);
						
						return new Real(from, to, step);
					}
				}
			}
		} catch(Exception e) {
			// NOTHING
		}
		
		throw new DeclarationException("Illegal declaration of Real Domain");
	}
	
	static private Cartesian makeCartesian(String domainDefinition, EntityContainer<Domain> domainContainer) throws UndefinedEntity, DeclarationException {
		String[] elements = domainDefinition.split(",");
		elements = trim(elements);
		
		Domain[] domains = new Domain[elements.length]; 
		
		for(int i = 0; i < elements.length; ++i) {
			domains[i] = domainContainer.get(elements[i]);
		}
		
		return new Cartesian(domains);
	}
}
