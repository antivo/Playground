package brodic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hr.fer.zemirs.fenc.set.FuzzySet;
import hr.fer.zemirs.fenc.set.builder.FuzzySetBuilderFromFunction;
import hr.fer.zemris.fenc.domain.DeclarationException;
import hr.fer.zemris.fenc.domain.Domain;
import hr.fer.zemris.fenc.domain.DomainException;

public class Concluder {
	
	
	private Domain domainOfRudder; //kormilo
	private Domain domainOfAcceleration;
	private Domain domainOfProbablyRightDirection;
	private Domain domainOfDistance;
	private Domain domainOfSpeed;
	
	private FuzzySet opasnoBlizu;
	private FuzzySet veryFast;
	
	private List<Rule> rules;
	
	public Concluder() throws DeclarationException, DomainException {
		domainOfRudder = new hr.fer.zemris.fenc.domain.Integer(-90, 90, 1);
		domainOfAcceleration = new hr.fer.zemris.fenc.domain.Integer(-100, 100, 1);
		domainOfProbablyRightDirection = new hr.fer.zemris.fenc.domain.Integer(0, 1, 1);
		domainOfDistance = new hr.fer.zemris.fenc.domain.Integer(0, 700, 1);
		domainOfSpeed = new hr.fer.zemris.fenc.domain.Integer(20, 50, 1);
		
		opasnoBlizu = FuzzySetBuilderFromFunction.build(domainOfDistance, "l", "20", "300");
		veryFast = FuzzySetBuilderFromFunction.build(domainOfSpeed, "gamma", "35", "45");
		
		rules = new ArrayList<Rule>();
		Map<String, FuzzySet> antecedens = new HashMap<String, FuzzySet>();
		// AKO SI OPASNO BLIZU (opcenito) USPORI
		antecedens.put("L", opasnoBlizu);
		antecedens.put("D", opasnoBlizu);
		antecedens.put("DL", opasnoBlizu);
		antecedens.put("LK", opasnoBlizu);
		
		FuzzySet fsA =  FuzzySetBuilderFromFunction.build(domainOfAcceleration, "pi", "-50", "-30", "-10", "0");
		FuzzySet fsK =  FuzzySetBuilderFromFunction.build(domainOfRudder, "pi", "-1", "0", "0", "1");
		
		rules.add(new Rule(antecedens, fsA, fsK));
		// Ako SI OPASNO BLIZU S DESNA USPORI I ZAKRENI NA LIJEVO
		antecedens.clear();
		antecedens.put("D", opasnoBlizu);
		antecedens.put("DL", opasnoBlizu);
		
		fsA =  FuzzySetBuilderFromFunction.build(domainOfAcceleration, "pi", "-50", "-30", "-10", "0");
		fsK =  FuzzySetBuilderFromFunction.build(domainOfRudder, "pi", "0", "20", "40", "60");
		
		rules.add(new Rule(antecedens, fsA, fsK));
		// Ako SI OPASNO BLIZU S LIJEVA USPORI I ZAKRENI NA DESNO
		antecedens.clear();
		antecedens.put("L", opasnoBlizu);
		antecedens.put("LK", opasnoBlizu);
		
		fsA =  FuzzySetBuilderFromFunction.build(domainOfAcceleration, "pi", "-50", "-30", "-10", "0");
		fsK =  FuzzySetBuilderFromFunction.build(domainOfRudder, "pi", "-60", "-40", "-20", "0");
		
		rules.add(new Rule(antecedens, fsA, fsK));
		
		//AKO SI JAKO BRZ UMJERI SE
		antecedens.clear();
		antecedens.put("V", veryFast);
		
		fsA =  FuzzySetBuilderFromFunction.build(domainOfAcceleration, "pi", "-50", "-30", "-10", "0");
		fsK =  FuzzySetBuilderFromFunction.build(domainOfRudder, "pi", "-2", "-1", "1", "2");
		
		rules.add(new Rule(antecedens, fsA, fsK));
	}
	
	
	// returns [a,k]
	public int[] conclude(Map<String, Double> m) throws Exception {
		List<FuzzySet> fsas = new ArrayList<FuzzySet>();
		List<FuzzySet> fsks = new ArrayList<FuzzySet>();
		for(Rule r : rules) {
			fsas.add(r.applyMinA(m));
			fsks.add(r.applyMinK(m));
		}
		
		FuzzySet A = Inferer.inferere(fsas);
		FuzzySet K = Inferer.inferere(fsks);
		
		
		int a = (int) Math.round(Inferer.CoA(A));
		int k = (int) Math.round(Inferer.CoA(K));
		
		return new int[] {a, k};
	}
}
