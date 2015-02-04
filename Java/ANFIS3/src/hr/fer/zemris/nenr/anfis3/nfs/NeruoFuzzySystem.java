package hr.fer.zemris.nenr.anfis3.nfs;

import java.util.List;

import hr.fer.zemris.nenr.anfis3.fs.FuzzySet;
import hr.fer.zemris.nenr.anfis3.fs.Rule;

public class NeruoFuzzySystem {
	private final int sizeOfRules = 81;
	
	private final double[] lastUa = new double[sizeOfRules];
	private final double[] lastUb = new double[sizeOfRules];
	private final double[] lastW = new double[sizeOfRules];
	private double[] lastWn; // normalized
	private final double[] lastF = new double[sizeOfRules];
	
	public int getSizeOfRules() {
		return sizeOfRules;
	}

	public double[] getLastUa() {
		return lastUa;
	}

	public double[] getLastUb() {
		return lastUb;
	}
	
	public double[] getLastWn() {
		return lastWn;
	}

	public double[] getLastW() {
		return lastW;
	}

	public double[] getLastF() {
		return lastF;
	}

	private double fireRule(int x, int y, Rule rule, int ruleIdx) {
		double a1 = rule.getA1();
		double b1 = rule.getB1();
		double c1 = rule.getC1();
		
		double a2 = rule.getA2();
		double b2 = rule.getB2();
		double c2 = rule.getC2();
		
		double ux = FuzzySet.u(x, a1, b1, c1);
		double uy = FuzzySet.u(y, a2, b2, c2);
		
		double t = FuzzySet.t(ux, uy);
		
		lastUa[ruleIdx] = ux;
		lastUb[ruleIdx] = uy;
		lastW[ruleIdx] = t;
		
		return t;
	}
	
	private double[] normalize(double[] un) {
		double sum = 0;
		for(int i = 0; i < un.length; ++i) {
			sum += un[i];
		}
		for(int i = 0; i < un.length; ++i) {
			un[i] /= sum;
		}
		
		this.lastWn = un;
		
		return un;
	}
	
	private double calculateF(int x, int y, Rule rule, int idxRule) {
		double p = rule.getP();
		double q = rule.getQ();
		double r = rule.getR();
		
		double f = p * x + q * y + r;
		
		this.lastF[idxRule] = f;
		
		return f; 
	}
	
	private double[] calculateUn(int x, int y, List<Rule> rules) {
		double[] un = new double[rules.size()];
		int idx = 0;
		for(Rule rule : rules) {
			un[idx] = fireRule(x, y, rule, idx);
			++idx;
		}
		return un;
	}
	
	private double caclulateOutput(int x, int y, double[] un, List<Rule> rules) {
		int idx = 0;
		double result = 0;
		for(Rule rule : rules) {
			result += un[idx] * calculateF(x, y, rule, idx);
			
		}
		return result;
	}
	
	public double output(int x, int y, List<Rule> rules) {
		double[] un = calculateUn(x, y, rules);
		un = normalize(un);
		
		double result = caclulateOutput(x, y, un, rules);
		return result;
	}
}
