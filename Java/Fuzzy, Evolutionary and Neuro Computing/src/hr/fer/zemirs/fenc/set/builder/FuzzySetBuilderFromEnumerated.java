package hr.fer.zemirs.fenc.set.builder;

import java.util.HashMap;
import java.util.Map;

import hr.fer.zemirs.fenc.set.FuzzySet;
import hr.fer.zemris.fenc.domain.DeclarationException;
import hr.fer.zemris.fenc.domain.Domain;

public class FuzzySetBuilderFromEnumerated extends FuzzySetBuilder {
	private static final String pairDelimiter = "/";
	
	private static String getElemDeclarationDelimiter(String declaration) {
		String elementDelimiter = null;
		if(declaration.contains("+")) {
			elementDelimiter = "\\+";
		} else if (declaration.contains(",")) { 
			elementDelimiter = ",";
		}
		
		return elementDelimiter;
	}
	
	private static String[] splitDeclarationByDelimiter(String declaration, String elementDelimiter) {
		String[] result = null;
		if(null == elementDelimiter) {
			result = new String[]{declaration};
		} else {
			result = declaration.split(elementDelimiter);	
		}
		
		return result;
	}
	
	private static String[] getElemDeclarations(String declaration) {
		String elementDelimiter = getElemDeclarationDelimiter(declaration);
		return splitDeclarationByDelimiter(declaration, elementDelimiter);
	}
	
	// remove enclosing '(' ')' 
	private static String getCleanElemDefiniton(String elemDefinition) {
		int last = elemDefinition.length() - 1;
		if('(' == elemDefinition.charAt(0) && elemDefinition.charAt(last) == ')') {
			elemDefinition = elemDefinition.substring(1, last);
		}
		return elemDefinition;
	}
	
	
	private static void asssertPair(String[] pair) throws DeclarationException {
		if(pair.length != 2) {
			throw new DeclarationException("When declaring element in fuzzy set you must write Double/String");
		}
	}
	
	private static Double getValueFromString(String value) throws DeclarationException {
		Double val = null;
		try {
			val = Double.parseDouble(value);
		} catch (Exception e) {
			throw new DeclarationException("Not possible to extract degree of membership");
		}
		
		return val;
	}
	
	private static String getElemFromString(String elem) {
		return getCleanElemDefiniton(elem);
	}
	
	private static void assertArgumentsForBuild(Domain domain, String definition) throws DeclarationException {
		if(domain == null) {
			throw new DeclarationException("Set can not be initialized without valid domain");
		}
		if(definition == null) {
			throw new DeclarationException("Set can not be initialized without definition");
		}
	}
	
	
	
	public static FuzzySet build(Domain domain, String definition) throws DeclarationException {
		assertArgumentsForBuild(domain, definition);
		
		Map<String, Double> set = getInitialFuzzySetContainer();
		if(!definition.equals("")) {
		String[] declarations = getElemDeclarations(definition);
			for(String declaration : declarations) {
				String[] pair = declaration.split(pairDelimiter);
				asssertPair(pair);
				
				Double value = getValueFromString(pair[0]);
				String elem = getElemFromString(pair[1]);
				
				if((elem.charAt(0) == '(') && (elem.charAt(elem.length() - 2) == ')')) {
					elem = elem.substring(1, elem.length()-2);
				}
				
				if(value > 1 || value < 0) {
					throw new DeclarationException("Element can belong to a set with a certany of [0,1]. " + value + " is out of bounds.");
				}
				
				if(value > 0) {
					set = addToSet(set, elem, value);
				}
			}
		}
		return FuzzySetBuilder.composeFuzzySet(domain, set);
	}
}
