package hr.fer.zemris.nenr.anfis3.fs;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FactoryRule {
	
	public static Rule createRandomRule(Random rand) {
		double a1 = rand.nextDouble();
		double b1 = rand.nextDouble();
		double c1 = rand.nextDouble();
		double a2 = rand.nextDouble();
		double b2 = rand.nextDouble(); 
		double c2 = rand.nextDouble();
		double p = rand.nextDouble();
		double q = rand.nextDouble(); 
		double r = rand.nextDouble();
		
		return new Rule(a1, b1, c1, a2, b2, c2, p, q, r);
	}
	
	public static List<Rule> createInitialRules(int numOfRules, Random rand) {
		List<Rule> rules = new ArrayList<Rule>();
		for(int i = 0; i < numOfRules; ++i) {
			Rule rule = createRandomRule(rand);
			rules.add(rule);
		}
		
		return rules;
	}
	
	public static Rule createRuleWIthCorrection(Rule rule, double a1Delta, double b1Delta, double c1Delta, double a2Delta, double b2Delta,
			double c2Delta, double pDelta, double qDelta, double rDelta) {
		
		double a1 = rule.getA1() + a1Delta;
		double b1 = rule.getB1() + b1Delta;
		double c1 = rule.getC1() + c1Delta;
		double a2 = rule.getA2() + a2Delta;
		double b2 = rule.getB2() + b2Delta;
		double c2 = rule.getC2() + c2Delta;
		 
		double p = rule.getP() + pDelta;
		double q = rule.getQ() + qDelta;
		double r = rule.getR() + rDelta;
	
		return new Rule(a1, b1, c1, a2, b2, c2, p, q, r);
	}
	
	public static List<Rule> correctRules(List<Rule> rules, double a1Delta, double b1Delta, double c1Delta, double a2Delta, double b2Delta,
			double c2Delta, double pDelta, double qDelta, double rDelta) {		
		
		for(int i = 0; i < rules.size(); ++i) {
			Rule rule = rules.get(i); 
			Rule ruleWithCorrection = createRuleWIthCorrection(rule, a1Delta, b1Delta, c1Delta, a2Delta, b2Delta, c2Delta, pDelta, qDelta, rDelta);
			rules.set(i, ruleWithCorrection);
		}
		
		return rules;
	}
}
