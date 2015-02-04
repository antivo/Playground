package hr.fer.zemris.fenc.domain;

public interface Domain {
	boolean equals(Domain other);
	boolean hasElement(String elem) throws DeclarationException;
	int getCardinality();
	String[] getDomainComponents();
	int getIndexOfElement(String value) throws DeclarationException, DomainException;
}
