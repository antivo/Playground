package hr.fer.zemris.nenr.anfis3.train;

import hr.fer.zemris.nenr.anfis3.dataset.Dataset;
import hr.fer.zemris.nenr.anfis3.fs.FactoryRule;
import hr.fer.zemris.nenr.anfis3.fs.Rule;
import hr.fer.zemris.nenr.anfis3.nfs.NeruoFuzzySystem;

import java.util.List;
import java.util.Random;

public class GradientDescent {
	private final double e;
	private final int  maxIteration;
	private final double step;
	
	public GradientDescent(double e, double step, int maxIteration) {
		this.e = e;
		this.maxIteration = maxIteration;
		this.step = step;
	}
	
	private static List<Rule> createInitialRules(int ruleNum, Random rand) {
		return FactoryRule.createInitialRules(ruleNum, rand);
	}
	
	public List<Rule> batchGradientDescent(int ruleNum, NeruoFuzzySystem nfs, Random rand, Dataset dataset) {
		List<Rule> rules = createInitialRules(ruleNum, rand);
		
		int iteration = 0;
		while(iteration < maxIteration) {
			++iteration;
			
			double sumError = 0;
			
			double deltaP = 0;
			double deltaQ = 0;
			double deltaR = 0;
			
			double deltaA1 = 0;
			double deltaA2 = 0;
			double deltaB1 = 0;
			double deltaB2 = 0;
			double deltaC1 = 0;
			double deltaC2 = 0;
			
			for(int idx = 0; idx < rules.size(); ++idx) {
				for(int x = dataset.getLow(); x <= dataset.getHigh(); ++x) {
					for(int y = dataset.getLow(); y <= dataset.getHigh(); ++y) {
						
						double f = dataset.get(x, y);
						double o = nfs.output(x, y, rules);
						double error = f - o;
						sumError += Math.abs(error) / 81;
						
						deltaP += fixConsequenceElement(nfs, idx, step, error, x);
						deltaQ += fixConsequenceElement(nfs, idx, step, error, y);
						deltaR += fixConsequenceElement(nfs, idx, step, error, 1);
						
						double a1 = rules.get(idx).getA1();
						double b1 = rules.get(idx).getB1();
						double c1 = rules.get(idx).getC1();
						
						deltaA1 += fixAntecedensElement(nfs, idx, f, error, aParamDerivation(x, a1, b1, c1));
						deltaB1 += fixAntecedensElement(nfs, idx, f, error, bParamDerivation(x, a1, b1, c1));
						deltaC1 += fixAntecedensElement(nfs, idx, f, error, cParamDerivation(x, a1, b1, c1));
						
						double a2 = rules.get(idx).getA2();
						double b2 = rules.get(idx).getB2();
						double c2 = rules.get(idx).getC2();
						
						deltaA2 += fixAntecedensElement(nfs, idx, f, error, aParamDerivation(x, a2, b2, c2));
						deltaB2 += fixAntecedensElement(nfs, idx, f, error, bParamDerivation(x, a2, b2, c2));
						deltaC2 += fixAntecedensElement(nfs, idx, f, error, cParamDerivation(x, a2, b2, c2));
					}
				}
			}
			
			System.out.println(sumError);
			if(Double.NaN == sumError) {
				break;
			}
			
			rules = FactoryRule.correctRules(rules, deltaA1, deltaB1, deltaC1, deltaA2, deltaB2, deltaC2, deltaP, deltaQ, deltaR);
			
			
			System.out.println(sumError);
			if(sumError < e) { 
				break;
			}
		}
		return rules;
	}
	
	public List<Rule> stohasticGradientDescent(int ruleNum, NeruoFuzzySystem nfs, Random rand, Dataset dataset) {
		List<Rule> rules = createInitialRules(ruleNum, rand);
		
		int iteration = 0;
		while(iteration < maxIteration) {
			++iteration;
			
			double sumError = 0;
			
			double deltaP = 0;
			double deltaQ = 0;
			double deltaR = 0;
			
			double deltaA1 = 0;
			double deltaA2 = 0;
			double deltaB1 = 0;
			double deltaB2 = 0;
			double deltaC1 = 0;
			double deltaC2 = 0;
			for(int idx = 0; idx < rules.size(); ++idx) {
				for(int x = dataset.getLow(); x <= dataset.getHigh(); ++x) {
					for(int y = dataset.getLow(); y <= dataset.getHigh(); ++y) {
		
						double f = dataset.get(x, y);
						double o = nfs.output(x, y, rules);
						double error = f - o;
						sumError += Math.abs(error) / 81;
						
						deltaP += fixConsequenceElement(nfs, idx, step, error, x);
						deltaQ += fixConsequenceElement(nfs, idx, step, error, y);
						deltaR += fixConsequenceElement(nfs, idx, step, error, 1);
						
						double a1 = rules.get(idx).getA1();
						double b1 = rules.get(idx).getB1();
						double c1 = rules.get(idx).getC1();
						

						deltaA1 += fixAntecedensElement(nfs, idx, f, error, aParamDerivation(x, a1, b1, c1));
						deltaB1 += fixAntecedensElement(nfs, idx, f, error, bParamDerivation(x, a1, b1, c1));
						deltaC1 += fixAntecedensElement(nfs, idx, f, error, cParamDerivation(x, a1, b1, c1));

						double a2 = rules.get(idx).getA2();
						double b2 = rules.get(idx).getB2();
						double c2 = rules.get(idx).getC2();
						
						deltaA2 += fixAntecedensElement(nfs, idx, f, error, aParamDerivation(y, a2, b2, c2));
						deltaB2 += fixAntecedensElement(nfs, idx, f, error, bParamDerivation(y, a2, b2, c2));
						deltaC2 += fixAntecedensElement(nfs, idx, f, error, cParamDerivation(y, a2, b2, c2));
						
					}
				}
				
				//System.out.println("A1 corr " + deltaA1);
				
				Rule rule = rules.get(idx);
				Rule correctedRule = FactoryRule.createRuleWIthCorrection(rule, deltaA1, deltaB1, deltaC1, deltaA2, deltaB2, deltaC2, deltaP, deltaQ, deltaR);
				rules.set(idx, correctedRule);
				
			}
			
			if(Double.NaN == sumError) {
				break;
			}
			
			System.out.println(sumError);
			if(sumError < e) { 
				break;
			}
		}
		return rules;
	
	}
	

	private static double fixConsequenceElement(NeruoFuzzySystem nfs, int idxRule, double step, double error, int input) {
		return -step * error * nfs.getLastWn()[idxRule] * input;
	}
	
	private static double fixAntecedensElement(NeruoFuzzySystem nfs, int idxRule, double step, double error, double derivation) {
		double fireRuleError = 0;
		double fireRuleSum = 0;
		for(int j = 0; j < nfs.getSizeOfRules(); ++j) {
			fireRuleSum += nfs.getLastW()[j];
			
			if(j != idxRule) {
				fireRuleError += (nfs.getLastF()[idxRule] - nfs.getLastF()[j]) * nfs.getLastW()[j];
				
			}
		}
		fireRuleError /= (fireRuleSum * fireRuleSum);
		
		double lastUa = nfs.getLastUa()[idxRule];
		double lastUb = nfs.getLastUb()[idxRule];
		double fuzzyConclusionError = lastUa / (lastUa + lastUb);
		
		
		return step * error * fireRuleError * fuzzyConclusionError * derivation;
	}
	
	private static double aParamDerivation(int x, double a, double b, double c) {
		double xSUBc = x - c;
		double xSUBcDIVa = xSUBc / a;
		double xSUBcDIVa_SQR = xSUBcDIVa * xSUBcDIVa;
		double xSUBcDIVa_SQR_POWb_NEGATIVE = - Math.pow(xSUBcDIVa_SQR, b);
		
		double b_MUL_xSUBcDIVa_SQR_POWbmin1_NEGATIVE = - b * Math.pow(xSUBcDIVa_SQR, b - 1);
		
		double expression = Math.exp(xSUBcDIVa_SQR_POWb_NEGATIVE) * b_MUL_xSUBcDIVa_SQR_POWbmin1_NEGATIVE * (2 * xSUBcDIVa) * (-xSUBcDIVa/a);
	
		return expression;
	}
	
	private static double bParamDerivation(int x, double a,  double b, double c) {
		double xSUBc = x - c;
		double xSUBcDIVa = xSUBc / a;
		double xSUBcDIVa_SQR = xSUBcDIVa * xSUBcDIVa;
		double xSUBcDIVa_SQR_POWb_NEGATIVE = - Math.pow(xSUBcDIVa_SQR, b);
		
		double expression = Math.exp(xSUBcDIVa_SQR_POWb_NEGATIVE) * xSUBcDIVa_SQR_POWb_NEGATIVE * Math.log(xSUBcDIVa_SQR);
		
		return expression;
	}
	
	private static double cParamDerivation(int x, double a, double b, double c) {
		double xSUBc = x - c;
		double xSUBcDIVa = xSUBc / a;
		double xSUBcDIVa_SQR = xSUBcDIVa * xSUBcDIVa;
		double xSUBcDIVa_SQR_POWb_NEGATIVE = - Math.pow(xSUBcDIVa_SQR, b);
		
		double b_MUL_xSUBcDIVa_SQR_POWbmin1_NEGATIVE = - b * Math.pow(xSUBcDIVa_SQR, b - 1);
		
		double expression = Math.exp(xSUBcDIVa_SQR_POWb_NEGATIVE) * b_MUL_xSUBcDIVa_SQR_POWbmin1_NEGATIVE * (2 * xSUBcDIVa) * (-1./a);
		
		return expression;
	}
}
