package hr.fer.zemris.fenc.definer;

import hr.fer.zemirs.fenc.container.EntityContainer;
import hr.fer.zemirs.fenc.container.UndefinedEntity;
import hr.fer.zemirs.fenc.set.FuzzySet;
import hr.fer.zemirs.fenc.set.builder.FuzzySetBuilderFromEnumerated;
import hr.fer.zemirs.fenc.set.builder.FuzzySetBuilderFromFunction;
import hr.fer.zemris.fenc.domain.DeclarationException;
import hr.fer.zemris.fenc.domain.Domain;
import hr.fer.zemris.fenc.domain.DomainException;

public class FuzzySetDefiner extends Definer {
	// @ d1 is {0/x1, 1/x2, 0.5/x3};
	// @ d3 is gamma(3,5);
	// expr !(f1 * f7);
	public static FuzzySet defineFuzzySet(String fuzzySetDefinition, EntityContainer<Domain> domainContainer, EntityContainer<FuzzySet> fuzzySetContainer) throws DeclarationException, UndefinedEntity, DomainException {
		int firstWhitespace = fuzzySetDefinition.indexOf(' ');
		String definitionType = fuzzySetDefinition.substring(0, firstWhitespace).trim();
		String restOfDefinition = fuzzySetDefinition.substring(firstWhitespace + 1).trim();
		
		switch(definitionType) {
		case "@": return atDefinition(restOfDefinition, domainContainer);
		case "expr": return exprDefinition(restOfDefinition, fuzzySetContainer);
		
		default: throw new DeclarationException("Unsuported type of Fuzzy Set definition");
		}
	}

	private static FuzzySet atDefinition(String definition, EntityContainer<Domain> domainContainer) throws DeclarationException, UndefinedEntity, DomainException {
		int firstWhitespace = definition.indexOf(' ');
		String domain = definition.substring(0, firstWhitespace).trim();
		definition = definition.substring(firstWhitespace + 1).trim();
		
		firstWhitespace = definition.indexOf(' ');
		String keyWordIS = definition.substring(0, firstWhitespace).trim();
		if(keyWordIS.equals("is")) {
			definition = definition.substring(firstWhitespace + 1).trim();
			if('{' == definition.charAt(0) ) {
				return fromEnumerated(domain, definition, domainContainer);
			} else {
				return fromFunction(domain, definition, domainContainer);
			}
		}
		
		throw new DeclarationException("Illegal definition of Fuzzy Set");
	}
	
	private static FuzzySet fromEnumerated(String domain, String definition, EntityContainer<Domain> domainContainer) throws UndefinedEntity, DeclarationException {
		int last = definition.length() - 1;
		if('{' == definition.charAt(0)) {
			if('}' == definition.charAt(last)) {
				definition = definition.substring(1, last);
				
				Domain d = domainContainer.get(domain);
				return FuzzySetBuilderFromEnumerated.build(d, definition);
			}
		}
		
		throw new DeclarationException("Illegal definition of Fuzzy Set with enumeration");
	}
	
	private static FuzzySet fromFunction(String domain, String definition, EntityContainer<Domain> domainContainer) throws UndefinedEntity, DeclarationException, DomainException {
		int firstBrace = definition.indexOf('(');
		String function = definition.substring(0, firstBrace).trim();
		definition = definition.substring(firstBrace + 1, definition.length() - 1).trim();
		
		String[] args = definition.split(",");
		args = trim(args);
		
		Domain d = domainContainer.get(domain);
		return FuzzySetBuilderFromFunction.build(d, function, args);
	}
	
	private static FuzzySet exprDefinition(String definition, EntityContainer<FuzzySet> fuzzySetContainer) {
		return null;
	}
}
