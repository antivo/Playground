package hr.fer.zemirs.fenc.set.builder;

import java.util.HashMap;
import java.util.Map;

import hr.fer.zemirs.fenc.set.FuzzySet;
import hr.fer.zemris.fenc.domain.DeclarationException;
import hr.fer.zemris.fenc.domain.Domain;
import hr.fer.zemris.fenc.domain.DomainException;

public class FuzzySetBuilderFromFunction {
	
	private static void assertRisingArguments(int[] args, int expectedArgs) throws DeclarationException {
		if(args.length != expectedArgs) {
			throw new DeclarationException("This function demands " + expectedArgs + " arguments");
		}
		
		int previous = args[0];
		for(int i = 1; i < expectedArgs; ++i) {
			int current = args[i];
			if(current < previous) {
				throw new DeclarationException("This function demands arguments to be in rising order");
			}
		}
	}
	
	private static int[] getIndices(Domain domain, String[] args) throws DeclarationException, DomainException {
		int length = args.length;
		int[] result = new int[length];
		for(int i = 0; i < length; ++i) {
			String argument = args[i];
			result[i] = domain.getIndexOfElement(argument);
		}
		return result;
	}
	
	private static Map<String, Double> gammaCompose(Domain domain, int[] args) throws DeclarationException {
		assertRisingArguments(args, 2);
		Map<String, Double> set = new HashMap<String,Double>();
		String[] elems = domain.getDomainComponents();
		final int alpha = args[0];
		final int beta = args[1];
		for(int i = alpha + 1; i < beta; ++i) {
			double res = (double) (i - alpha) / (beta - alpha);
			set.put(elems[i], new Double(res));
		}
		for(int i = beta; i < elems.length; ++i) {
			set.put(elems[i], new Double(1));
		}
		
		return set;
	}
	
	private static Map<String, Double> lambdaCompose(Domain domain, int[] args) throws DeclarationException {
		assertRisingArguments(args, 3);
		Map<String, Double> set = new HashMap<String,Double>();

		String[] elems = domain.getDomainComponents();
		final int alpha = args[0];
		final int beta = args[1];
		final int gamma = args[2];
		for(int i = alpha + 1; i < beta; ++i) {
			double res = (double) (i - alpha) / (beta - alpha);
			set.put(elems[i], new Double(res));
		}
		for(int i = beta; i < gamma; ++i) {
			double res = (double) (gamma - i) / (gamma - alpha);
			set.put(elems[i], new Double(res));
		}
		
		return set;
	}
	
	private static Map<String, Double> lCompose(Domain domain, int[] args) throws DeclarationException {
		assertRisingArguments(args, 2);
		Map<String, Double> set = new HashMap<String,Double>();

		String[] elems = domain.getDomainComponents();
		final int alpha = args[0];
		final int beta = args[1];
		
		for(int i = 0; i < alpha; ++i) {
			set.put(elems[i], new Double(1));
		}
		for(int i = alpha; i < beta; ++i) {
			double res = (double) (beta - i) / (beta - alpha);
			set.put(elems[i], new Double(res));
		}
		
		return set;
	}
	
	private static Map<String, Double> piCompose(Domain domain, int[] args) throws DeclarationException {
		assertRisingArguments(args, 4);
		Map<String, Double> set = new HashMap<String,Double>();

		String[] elems = domain.getDomainComponents();
		final int alpha = args[0];
		final int beta = args[1];
		final int gamma = args[2];
		final int delta = args[3];
		for(int i = alpha + 1; i < beta; ++i) {
			double res = (double) (i - alpha) / (beta - alpha);
			set.put(elems[i], new Double(res));
		}
		for(int i = beta; i < gamma; ++i) {
			set.put(elems[i], new Double(1));
		}
		for(int i = gamma; i < delta; ++i) {
			double res = (double) (delta - i) / (delta - gamma);
			set.put(elems[i], new Double(res));
		}
		
		return set;
	}
	
	public static FuzzySet build(Domain domain, String function, String... args) throws DeclarationException, DomainException {
		int[] indices = getIndices(domain, args);
		
		Map<String, Double> set = null;
		switch(function) {
		case "gamma"  : set =gammaCompose(domain, indices);
		                break;
		case "lambda" : set = lambdaCompose(domain, indices);
		                break;
		case "l"      : set = lCompose(domain, indices);
		                break;
		case "pi"     : set = piCompose(domain, indices);
		                break;
		default: throw new DeclarationException("Selected function for declaring fuzzy sets does not exist");
		}
		return FuzzySetBuilder.composeFuzzySet(domain, set);
	}
}
