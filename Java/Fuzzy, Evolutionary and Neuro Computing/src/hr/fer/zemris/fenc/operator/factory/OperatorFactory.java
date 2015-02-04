package hr.fer.zemris.fenc.operator.factory;

import hr.fer.zemris.fenc.domain.DeclarationException;
import hr.fer.zemris.fenc.operator.Operator;

public interface OperatorFactory {
	public Operator produce(String name, double... params) throws DeclarationException;
}
