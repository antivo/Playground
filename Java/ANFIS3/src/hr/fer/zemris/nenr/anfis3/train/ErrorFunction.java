package hr.fer.zemris.nenr.anfis3.train;



public class ErrorFunction {
	static public double error(int y, int o) {
		return 0.5 * (y - o) * (y - o);
	}
}
