package hr.fer.zemris.fenc.operator.factory;

import hr.fer.zemirs.fenc.set.FuzzySet;
import hr.fer.zemirs.fenc.set.builder.FuzzySetBuilder;
import hr.fer.zemris.fenc.domain.Cartesian;
import hr.fer.zemris.fenc.domain.DeclarationException;
import hr.fer.zemris.fenc.domain.Domain;
import hr.fer.zemris.fenc.domain.DomainException;
import hr.fer.zemris.fenc.element.CartesianElement;
import hr.fer.zemris.fenc.operator.Operator;

import java.util.Map;

public class CartesianOperatorFactory extends FuzzySetBuilder implements OperatorFactory {

	private static void assertMamdani(double... params) throws DeclarationException {
		if(0 != params.length) {
			throw new DeclarationException("Mamdani does not use parameters");
		}
	}
	
	@Override
	public Operator produce(String name, double... params) throws DeclarationException {
		switch(name) {
		case "Mamdani" : return mamdani(params);
		
		default: throw new DeclarationException("Unsupported operator " + name);
		}
	}

	private Operator mamdani(final double[] params) throws DeclarationException {
		assertMamdani(params);
		
		return new  Operator() {			
			@Override
			public FuzzySet operate(FuzzySet... args) throws DeclarationException, DomainException {
				assertOperate(args);
				FuzzySet leftSet = args[0];
				FuzzySet rightSet = args[1];
				
				Domain leftDomain = leftSet.getDomain();
				Domain rightDomain = rightSet.getDomain();
				
				Domain domain = new Cartesian(leftDomain, rightDomain);
				Map<String, Double> set = getInitialFuzzySetContainer();
				
				String[] componenents = domain.getDomainComponents();
				for(String component : componenents) {
					String[] elements = CartesianElement.getElemsFromProduct(component);
					
					String fst = elements[0];
					String snd = elements[1];
					
					double a = leftSet.getMembershipFor(fst);
					double b = rightSet.getMembershipFor(snd);
					
					double value = Math.min(a, b);
					

					set = addToSet(set, component, value);	
				}
								
				return createFuzzySet(domain, set);
			}

			private void assertOperate(FuzzySet... args) throws DeclarationException {
				if(2 != args.length) {
					throw new DeclarationException("HamacherS operates on two fuzzy set.");
				}
			}
		};
	}
	
}
