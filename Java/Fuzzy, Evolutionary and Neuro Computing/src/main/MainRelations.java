package main;

import hr.fer.zemirs.fenc.container.EntityContainer;
import hr.fer.zemirs.fenc.container.UndefinedEntity;
import hr.fer.zemirs.fenc.relation.RelationProjection;
import hr.fer.zemirs.fenc.relation.RelationTester;
import hr.fer.zemirs.fenc.set.FuzzySet;
import hr.fer.zemris.fenc.definer.DomainDefiner;
import hr.fer.zemris.fenc.definer.FuzzySetDefiner;
import hr.fer.zemris.fenc.domain.DeclarationException;
import hr.fer.zemris.fenc.domain.Domain;
import hr.fer.zemris.fenc.domain.DomainException;
import hr.fer.zemris.fenc.operator.Operator;
import hr.fer.zemris.fenc.operator.factory.AdditionOperatorFactory;
import hr.fer.zemris.fenc.operator.factory.NegationOperatorFactory;
import hr.fer.zemris.fenc.operator.factory.OperatorFactory;

public class MainRelations {

	public static void main(String[] args) throws DeclarationException, UndefinedEntity, DomainException {
		EntityContainer<Domain> domainContainer = new EntityContainer<Domain>();
		EntityContainer<FuzzySet> fuzzySetContainer = new EntityContainer<FuzzySet>();
		EntityContainer<Operator> operatorContainer = new EntityContainer<Operator>();
		
		Domain d1 = DomainDefiner.defineDomain("enumerated {x1, x2, x3, x4}", domainContainer);
		domainContainer.set("d1", d1);
		Domain d2 = DomainDefiner.defineDomain("cartesian d1,d1", domainContainer);
		domainContainer.set("d2", d2);
		
		
		FuzzySet marko = FuzzySetDefiner.defineFuzzySet("@ d1 is {1/(x1) + 0.5/x2}", domainContainer, fuzzySetContainer);
		
	
		OperatorFactory operatorFactory = new NegationOperatorFactory();
		Operator not = operatorFactory.produce("ZadehNot");
		
		FuzzySet noviFuzzySet = not.operate(marko);
		
		operatorFactory = new AdditionOperatorFactory();
		Operator and = operatorFactory.produce("HamacherS", 0.5);
		
		FuzzySet result = and.operate(marko, noviFuzzySet);
		
		for(String s : d1.getDomainComponents()) {
			System.out.println(s + " " + result.getMembershipFor(s));
		}
		
		
		/*FuzzySet f1 = FuzzySetDefiner.defineFuzzySet("@ d2 is {1/(x1,x1) + 1/(x2,x2) + 1/(x3,x3) + 1/(x4,x4) + 0.5/(x3,x4) + 0.5/(x4,x3) + 0.8/(x3,x2)}", domainContainer, fuzzySetContainer);
		fuzzySetContainer.set("f1", f1);
		
		/*for(String x : d2.getDomainComponents()) {
			System.out.println(x + " " +f1.getMembershipFor(x));
		}
		
		System.out.print("skup ");
		if(!RelationTester.testSymetric(f1)) {
			System.out.print("ni");
		}
		System.out.println("je simetrican");
		
		System.out.print("skup ");
		if(!RelationTester.testReflexive(f1)) {
			System.out.print("ni");
		}
		System.out.println("je refleksivan");
		
		String method = "max-min";
		System.out.print("skup ");
		if(RelationTester.testTransitive(f1, method)) {
			System.out.print("ni");
		}
		System.out.println("je " + method + " tranzitivan");
		
		System.out.print("skup ");
		if(RelationTester.testTransitive(f1, method)) {
			System.out.print("ni");
		}
		System.out.println("je " + method + " ekvivalentan");
		
		
		
		Domain d4 = DomainDefiner.defineDomain("integer -2 to 10 step 1", domainContainer);
		domainContainer.set("d4", d4);
		Domain d5 = DomainDefiner.defineDomain("cartesian d1,d4,d1", domainContainer);
		domainContainer.set("d5", d5);
		Domain d6 = DomainDefiner.defineDomain("cartesian d4,d1", domainContainer);
		domainContainer.set("d6", d6);
		
		FuzzySet f2 = FuzzySetDefiner.defineFuzzySet("@ d5 is {0.1/(x1,2,x1) + 1/(x2,2,x2) + 1/(x3,3,x3)}", domainContainer, fuzzySetContainer);
		fuzzySetContainer.set("f2", f2);

		FuzzySet f3 = RelationProjection.projectRelation(f2, d6);
		
// model baze pravila 
		
		pravila + baza pravila*/
	}

}
