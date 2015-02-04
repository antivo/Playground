package hr.fer.zemris.nenr.anfis3.fs;

public class Rule {
	private final double a1;
	private final double b1; 
	private final double c1;
	
	private final double a2;
	private final double b2; 
	private final double c2;
	
	private final double p; 
	private final double q; 
	private final double r;
	
	public Rule(double a1, double b1, double c1, double a2, double b2,
			double c2, double p, double q, double r) {
		super();
		this.a1 = a1;
		this.b1 = b1;
		this.c1 = c1;
		this.a2 = a2;
		this.b2 = b2;
		this.c2 = c2;
		this.p = p;
		this.q = q;
		this.r = r;
	}
	
	public double getA1() {
		return a1;
	}
	
	public double getB1() {
		return b1;
	}
	
	public double getC1() {
		return c1;
	}
	
	public double getA2() {
		return a2;
	}
	
	public double getB2() {
		return b2;
	}
	
	public double getC2() {
		return c2;
	}
	
	public double getP() {
		return p;
	}
	
	public double getQ() {
		return q;
	}
	
	public double getR() {
		return r;
	}
}
