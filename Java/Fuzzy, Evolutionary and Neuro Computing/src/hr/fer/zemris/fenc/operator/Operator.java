package hr.fer.zemris.fenc.operator;

import hr.fer.zemirs.fenc.set.FuzzySet;
import hr.fer.zemirs.fenc.set.builder.FuzzySetBuilder;
import hr.fer.zemris.fenc.domain.DeclarationException;
import hr.fer.zemris.fenc.domain.DomainException;

public interface Operator {
	public FuzzySet operate(FuzzySet... args) throws DeclarationException, DomainException;
}
