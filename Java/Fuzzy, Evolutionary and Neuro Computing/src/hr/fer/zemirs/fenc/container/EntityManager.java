package hr.fer.zemirs.fenc.container;

import hr.fer.zemirs.fenc.set.FuzzySet;
import hr.fer.zemris.fenc.domain.DeclarationException;
import hr.fer.zemris.fenc.domain.Domain;
import hr.fer.zemris.fenc.operator.Operator;

public class EntityManager {
	private static String[] strictlyAllowedOperators = new String[] {"!", "+", "*", "->"};
	
	private final EntityContainer<Domain> domainContainer = new EntityContainer<Domain>();
	private final EntityContainer<FuzzySet> fuzzySetContainer = new EntityContainer<FuzzySet>();
	private final EntityContainer<Operator> operatorContainer = new EntityContainer<Operator>(strictlyAllowedOperators);
	
	
	public void setDomain(String domainName, Domain domain) throws DeclarationException {
		domainContainer.set(domainName, domain);
	}
	
	public Domain getDomain(String domainName) throws UndefinedEntity {
		return domainContainer.get(domainName);
	}
	
	public void setFuzzySet(String fuzzySetName, FuzzySet fuzzySet) throws DeclarationException {
		fuzzySetContainer.set(fuzzySetName, fuzzySet);
	}
	
	public FuzzySet getFuzzySet(String fuzzySetName) throws UndefinedEntity {
		return fuzzySetContainer.get(fuzzySetName);
	}
	
	public void setOperator(String operatorSymbol, Operator operator) throws DeclarationException {
		operatorContainer.set(operatorSymbol, operator);
	}
	
	public Operator getOperator(String operatorSymbol) throws UndefinedEntity {
		return operatorContainer.get(operatorSymbol);
	}
	
	public boolean containsDomain(String domainName) {
		return domainContainer.contains(domainName);
	}
	 
	public boolean containsFuzzySet(String fuzzySetName) {
		return fuzzySetContainer.contains(fuzzySetName);
	}
	
	public boolean containsOperator(String operatorName) {
		return operatorContainer.contains(operatorName);
	}
}
