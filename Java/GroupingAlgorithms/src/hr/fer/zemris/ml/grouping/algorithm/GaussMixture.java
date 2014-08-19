package hr.fer.zemris.ml.grouping.algorithm;

import java.util.List;

import Jama.Matrix;

public class GaussMixture {
	public double pi;
	public Matrix u;
	public Matrix E;
	
	public GaussMixture(double pi, Matrix u, Matrix e) {
		super();
		this.pi = pi;
		this.u = u;
		E = e;
	}
	
	public double  responsibility(Matrix x) {
		Matrix x_u = x.minus(u);
		Matrix x_uT = x_u.transpose();
		Matrix E_1 = E.inverse();
		
		Matrix matRez = x_uT.times(E_1).times(x_u).times(-1./2.);
		
		double res = matRez.get(0, 0);
		res = Math.exp(res);
		
		double n = Math.pow(2 * Math.PI, x.getRowDimension() / 2) * Math.sqrt(E.det());
		
		return res/n * pi;
	}
	
	public double h(Matrix x, List<GaussMixture> ls) {
		
		
		double b = responsibility(x);
		
		double n = 0;
		for(int i = 0; i < ls.size(); ++i) {
			n += ls.get(i).responsibility(x);
		}
		
		
		return b/n;
	}
}
