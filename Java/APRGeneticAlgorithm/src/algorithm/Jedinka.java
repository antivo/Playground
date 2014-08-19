package algorithm;

import java.util.Random;

import function.IFunction;

public class Jedinka {
	public double[] koordinate;
	private final Random rand;

	private final double lowerBound = -100;
	private final double upperBound = 100;

	
	public Jedinka(Random r, int dimm) {
		rand = r;
		koordinate = new double[dimm];
		
		for(int i = 0; i < dimm; ++i) {
			this.koordinate[i] = rand.nextDouble() * (upperBound - lowerBound) + lowerBound;
		}
	}
	
	double getFitness(IFunction f) {
		return f.calculate(koordinate);
	}
	
	public double normalize(double num) {
		if(num > upperBound) {
			return upperBound;
		} else if(num < lowerBound) {
			return lowerBound;
		}
		return num;
	}
	
	public void mutate(double pm) {
		double raspon = upperBound - lowerBound;
		for(int i = 0; i < koordinate.length; ++i) {
			if(pm > rand.nextDouble()) {
				double trial = koordinate[i];
				trial += (rand.nextGaussian() - 0.5) * raspon;
				trial = normalize(trial);
				
				koordinate[i] = trial;
			}
		}
	}
	
	public void print() {
		StringBuilder out = new StringBuilder("");
		for(int i = 0; i < koordinate.length; ++i) {
			out.append(String.format("%.3f", koordinate[i])).append(" ");
		}
		System.out.println(out);
	}
	
}
