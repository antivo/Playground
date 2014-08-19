package hr.fer.zemris.fenc.operator.factory;

import java.util.Map;

import hr.fer.zemirs.fenc.set.FuzzySet;
import hr.fer.zemirs.fenc.set.builder.FuzzySetBuilder;
import hr.fer.zemris.fenc.domain.DeclarationException;
import hr.fer.zemris.fenc.domain.Domain;
import hr.fer.zemris.fenc.domain.DomainException;
import hr.fer.zemris.fenc.operator.Operator;

public class AdditionOperatorFactory extends FuzzySetBuilder implements OperatorFactory {

	private static void assertHamacherS(double... params) throws DeclarationException {
		if(1 != params.length) {
			throw new DeclarationException("HamacherS demands one parameter");
		}
	}
	
	@Override
	public Operator produce(String name, double... params) throws DeclarationException {
		switch(name) {
		case "HamacherS" : return hamacherS(params);
		
		default: throw new DeclarationException("Unsupported operator " + name);
		}
	}

	private Operator hamacherS(final double[] params) throws DeclarationException {
		assertHamacherS(params);
		
		return new  Operator() {
			final protected double v = params[0];
			
			@Override
			public FuzzySet operate(FuzzySet... args) throws DeclarationException, DomainException {
				assertOperate(args);
				FuzzySet leftSet = args[0];
				FuzzySet rightSet = args[1];
				
				Domain domain = leftSet.getDomain();
				Domain domainOther = rightSet.getDomain();
				if (domain.equals(domainOther)) {
				
				
					String[] componenents = domain.getDomainComponents();
					
					Map<String, Double> set = getInitialFuzzySetContainer();
					for(String component : componenents) {
						double a = leftSet.getMembershipFor(component);
						double b = rightSet.getMembershipFor(component);
						
						double value = (a + b - (2 - v) * a * b) / (1 - (1 - v) * a * b);
						
						set = addToSet(set, component, value);
					}
								
					return createFuzzySet(domain, set);
				} 
				
				throw new DeclarationException("Two given sets do not belong to the same domain");
			}

			private void assertOperate(FuzzySet... args) throws DeclarationException {
				if(2 != args.length) {
					throw new DeclarationException("HamacherS operates on two fuzzy set.");
				}
			}
		};
	}
	
}
