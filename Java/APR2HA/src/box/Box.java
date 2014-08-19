package box;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import f.IFunction;

public class Box {
	static private Random random = new Random(); 
	
	static final double upper = 100;
	static final double down = -100; 
	
	static private void assertCoord(double[] coord) throws IllegalArgumentException {
		if(coord.length < 2) {
			throw new IllegalArgumentException("Box has constraints that demand at least 2 arguments");
		}
	}
	
	static private boolean implicitConstraint(double[] coord) {
		boolean ret = true;
		
		double x1 = coord[0];
		double x2 = coord[1];
		
		if(x1 - x2 > 0) {
			ret = false;
		}
		
		if(x1 - 2 > 0) {
			ret = false;
		}
		
		return ret;
	}
	
	static private boolean explicitConstraint(double[] x) {
		int numberOfDimm = x.length;
		
		for(int i = 0; i < numberOfDimm; ++i) {
			double xi = x[i];
			if(xi < down || xi > upper) {
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
	
	static private double[] pomakniTocku(double[] tocka, double[] centroid) {
		double[] tx = new double[tocka.length];
        
        for (int i = 0; i < tocka.length; i++) {
            tx[i] = /*rand.nextDouble() */ 0.5 * (tx[i] + centroid[i]);
        }
        return tx;
	}
	
	static private double[] izracunajCentroid(List<double[]> x2n) {
		int numberOfDimm = x2n.get(0).length;
		double[] centroid = new double[numberOfDimm];
        
		for (int i = 0; i < numberOfDimm; i++) {
            centroid[i] = 0;
        }
		
        for(double[] coord : x2n) {
            for (int i = 0; i < numberOfDimm; i++) {
                centroid[i] += coord[i];
            }
        }
        
        for (int i = 0; i < numberOfDimm; i++) {
            centroid[i] /= numberOfDimm;
        }
        
        return centroid;
	}
	
	static private int indeksNajlosijeg(IFunction fun, List<double[]> tocke) {
        int najlosiji = 0;

        double H = fun.calculate(tocke.get(najlosiji));
        int i = 0;
        for (double[] coord : tocke) {
        	double F = fun.calculate(coord);
        	
            if (F > H) {
                najlosiji = i;
                H = F;
            }
            ++i;
        }
        
        return najlosiji;
    }

    static private int indeksDrugogNajlosijeg(IFunction fun, List<double[]> tocke, int indeksNajlosijeg) {
        int drugiNajlosiji;
        double H;
        
        if (indeksNajlosijeg == 0) {
            drugiNajlosiji = 1;
        } else {
            drugiNajlosiji = 0;
        }
        H = fun.calculate(tocke.get(drugiNajlosiji));

        int i = 0;
        for (double[] coord : tocke) {
            if (i == indeksNajlosijeg) continue;
            
            double F = fun.calculate(coord);
            if (F > H) {
                drugiNajlosiji = i;
                H = F;
            }
            
            ++i;
        }
        
        return drugiNajlosiji;
    }
    
    static private double[] izracunajCentroidBezNajlosijeg(List<double[]> tocke, int indeksNajlosijeg) {
    	int numOfDimm = tocke.get(0).length;
    	
    	double[] centroid = new double[numOfDimm];
        for (int i = 0; i < numOfDimm; i++) {
            centroid[i] = 0;
        }
        
        for (int i=0; i< numOfDimm; i++) {
            if (i == indeksNajlosijeg) continue;
            
            for (int j = 0; j < indeksNajlosijeg; j++) {
                centroid[j] += tocke.get(i)[j];
            }
        }
        
        for (int i = 0; i < numOfDimm; i++) {
            centroid[i] /= (numOfDimm - 1);
        }
        
        return centroid;
    }
    
    static private double[] vratiXr(double[] xc, double[] xh, double k) {
    	int numOfDim = xc.length;
    	
        double rez[] = new double[numOfDim];
        for (int i = 0; i < numOfDim; i++) {
            rez[i] = (1.0 + k) * xc[i] - k * xh[i];
        }
        
        return rez;
    }
	
	static public double[] box(IFunction fun, double[] x0, double alpha, double e) throws IllegalArgumentException {
		assertCoord(x0);
		if(explicitConstraint(x0) && implicitConstraint(x0)) {
			double[] xc = copyArray(x0);
			List<double[]> x2n = new ArrayList<double[]>();
			int n = xc.length;
			for(int t = 0; t < 2 * n; ++t) {
				double[] novaTocka = new double[n];
				for (int i = 0; i < n; i++) {
					novaTocka[i] = down + random.nextDouble() * (upper - down);
				}
				
				while (!implicitConstraint(novaTocka)) {
					novaTocka = pomakniTocku(novaTocka, xc);
                }
				
				x2n.add(novaTocka);
				
				xc = izracunajCentroid(x2n);
			}
			//
			boolean uvjet = true;
            do
            {
                int h = indeksNajlosijeg(fun, x2n);
                int h2 = indeksDrugogNajlosijeg(fun, x2n, h);
                xc = izracunajCentroidBezNajlosijeg(x2n, h);
                double[] xr = vratiXr(xc, x2n.get(h), alpha);
                for (int i = 0; i < n; i++) {
                    if (xr[i] < -100) {
                        xr[i] = -100;
                    } else if (xr[i] > 100) {
                        xr[i] = 100;
                    }
                }

                while (!implicitConstraint(xr) || !explicitConstraint(xr)) {
                    xr = pomakniTocku(xr, xc);
                }
                
                if (fun.calculate(xr) > fun.calculate(x2n.get(h2))) {
                    xr = pomakniTocku(xr, xc);
                }
                
                for(int i = 0; i < n; ++i) {
                	x2n.get(h)[i] = xr[i];
                }

                uvjet = false;
                for (int i = 0; i < n; i++)
                    if (Math.abs(x2n.get(h)[i] - xc[i]) > e)
                        uvjet = true;

            }
            while (uvjet);
			//
           
            return xc;
		} else {
			throw new IllegalAccessError("Constraints are not satisfied");
		}
	}
}
