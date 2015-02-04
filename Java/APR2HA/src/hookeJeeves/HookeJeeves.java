package hookeJeeves;

import f.IFunction;


// hj1.txt hj2.txt hj3.txt hj4.txt

/*
x0 - pocetna tocka
xB - bazna tocka 
xP - pocetna tocka pretrazivanja
xN - tocka dobivena pretrazivanjem
 */
public class HookeJeeves {
	static public double[] istrazi(IFunction fun, double[] dx, double[] xp) {
		int numberOfDimm = xp.length;
		
		double[] x = new double[numberOfDimm];
		for(int i = 0; i < numberOfDimm; ++i) {
			x[i] = xp[i];
		}
		
		for(int i = 0; i < numberOfDimm; ++i) {
			double P = fun.calculate(x);
			x[i] = x[i] + dx[i];       // povecamo za Dx
			double N = fun.calculate(x);
			if(N > P) {             // ne valja pozitivni pomak
				x[i] = x[i] - 2*dx[i];  // smanjimo za Dx
				N = fun.calculate(x);
				if(N > P) {          // ne valja ni negativni
					x[i] = x[i] + dx[i];  // vratimo na staro
				}
			}
		}
		
		return x;
	}
	
	static private double[] smanji(double[] dx) {
		double[] ret = new double[dx.length];
		for(int i = 0; i < dx.length; ++i) {
			ret[i] = dx[i] / 2.;
		}
		return ret;
	}
	
	static private boolean isLessOrEqThan(double[] dx, double[] e) {
		for(int i = 0; i < dx.length; ++i) {
			if(dx[i] > e[i]) {
				return false;
			}
		}
		return true;
	}
	
	static private double[] copyArray(double[] arg) {
		double[] ret = new double[arg.length];
		
		for(int i = 0; i < arg.length; ++i) {
			ret[i] = arg[i];
		}
		
		return ret;
	}
	
	static private void printTocku(double[] tocka) {
		System.out.println("\nIspis tocke");
		for(int i = 0; i < tocka.length; ++i) {
			System.out.print(tocka[i] + " ");
		}
	}
	
	static public double[] hookeJeeves(IFunction fun, double[] x0, double[] dx, double[] e) {
		int numberOfDimm = x0.length;
		
		double[] xp = copyArray(x0);
		double[] xb = copyArray(x0);
		
		do {
			double[] xn = istrazi(fun, dx, xp);   // definiran je potprogram
			
			printTocku(xn);
			
			if (fun.calculate(xn) < fun.calculate(xb)) {   // prihvacamo baznu tocku
			    for(int i = 0; i < numberOfDimm; ++i) { 
			    	xp[i] = 2*xn[i] - xb[i];  // definiramo novu tocku pretrazivanja
			    }
			    xb = copyArray(xn);
			} else {
			  dx = smanji(dx);
			  xp = copyArray(xb);        // vracamo se na zadnju baznu tocku
			}
		} while(!isLessOrEqThan(dx, e));
		
		return xb;
	}
}
