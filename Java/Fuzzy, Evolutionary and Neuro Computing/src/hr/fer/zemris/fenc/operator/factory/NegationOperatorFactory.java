package hr.fer.zemris.fenc.operator.factory;

import java.util.Map;

import hr.fer.zemirs.fenc.set.FuzzySet;
import hr.fer.zemirs.fenc.set.builder.FuzzySetBuilder;
import hr.fer.zemris.fenc.domain.DeclarationException;
import hr.fer.zemris.fenc.domain.Domain;
import hr.fer.zemris.fenc.domain.DomainException;
import hr.fer.zemris.fenc.operator.Operator;

public class NegationOperatorFactory extends FuzzySetBuilder implements OperatorFactory {
	@Override
	public Operator produce(String name, double... params) throws DeclarationException {
		switch(name) {
		case "ZadehNot" : return zadehNot(params);
		
		default: throw new DeclarationException("Unsupported operator " + name);
		}
	}

	private static void assertZadehNot(double... params) throws DeclarationException {
		if(0 != params.length) {
			throw new DeclarationException("ZadehNot does not use parameters");
		}
	}
	
	private Operator zadehNot(double[] params) throws DeclarationException {
		assertZadehNot(params);
		
		return new  Operator() {
			@Override
			public FuzzySet operate(FuzzySet... args) throws DeclarationException, DomainException {
				assertOperate(args);
				FuzzySet argSet = args[0];
				
				Domain domain = argSet.getDomain();
				String[] componenents = domain.getDomainComponents();
				
				Map<String, Double> set = getInitialFuzzySetContainer();
				for(String component : componenents) {
					double value = 1 - argSet.getMembershipFor(component);
					set = addToSet(set, component, value);
				}
							
				return createFuzzySet(domain, set);
			}

			private void assertOperate(FuzzySet... args) throws DeclarationException {
				if(1 != args.length) {
					throw new DeclarationException("ZadehNot operates on one fuzzy set.");
				}
			}
		};
	}
	
}
