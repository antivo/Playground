package main;

import java.io.ObjectInputStream.GetField;
import java.math.MathContext;

import hr.fer.zemirs.fenc.set.FuzzySet;
import hr.fer.zemirs.fenc.set.builder.FuzzySetBuilderFromEnumerated;
import hr.fer.zemirs.fenc.set.builder.FuzzySetBuilderFromFunction;
import hr.fer.zemris.fenc.domain.Cartesian;
import hr.fer.zemris.fenc.domain.DeclarationException;
import hr.fer.zemris.fenc.domain.Domain;
import hr.fer.zemris.fenc.domain.DomainException;
import hr.fer.zemris.fenc.domain.Enumerated;
import hr.fer.zemris.fenc.domain.Integer;
import hr.fer.zemris.fenc.operator.Operator;
import hr.fer.zemris.fenc.operator.factory.CartesianOperatorFactory;
import hr.fer.zemris.fenc.operator.factory.NegationOperatorFactory;
import hr.fer.zemris.fenc.operator.factory.OperatorFactory;

public class MainTestFuzzySet {

	public static void main(String[] args) throws DeclarationException, DomainException {
		//try {
		Domain a1 = new Integer(2, 4, 1);
		FuzzySet fs = FuzzySetBuilderFromEnumerated.build(a1, "0.123/2,   0.123/3");

		OperatorFactory negFac = new CartesianOperatorFactory();
		Operator mamd = negFac.produce("Mamdani");

		
		System.out.println(fs.getMembershipFor("3"));
		
		//System.out.println(fs.getMembershipFor("2"));
		//} catch(Exception e) {
			//System.out.println(e.getMessage());
		//}
		
		//System.out.println("222".compareTo("25.5444"));
		//System.out.println(Double.parseDouble("25"));
		
		Domain d = new Enumerated("x1", "x2", "x3", "x4");
		
		FuzzySet fs2 = FuzzySetBuilderFromFunction.build(d, "pi", new String[]{"x1", "x2","x3", "x4"});
		
		for(String s : d.getDomainComponents()) {
			System.out.println(s + "  "  + fs2.getMembershipFor(s));
		}
		
		System.out.println("=====");
		
		FuzzySet result = mamd.operate(fs, fs2);
		for(String s : result.getDomain().getDomainComponents()) {
			System.out.println(s + "  "  + result.getMembershipFor(s));
		}	
		
		
		for(String s : "k,a,g".split(",")) {
			System.out.println(s);
		}
	}

}
