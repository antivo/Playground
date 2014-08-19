package hr.fer.zemris.nenr.anfis3.fs;

public class FuzzySet {
	// 3
	static public double u(int x, double a, double b, double c) {
		double res = (x - c) / a;
		res *= res;
		
		res = - Math.pow(res, b);
		
		return Math.exp(res);
	}
	
	//Hamacher
	static public double t(double ua, double ub) {
		double res = (ua * ub) / (ua + ub - ua* ub);
		
		return res;
	}
}
