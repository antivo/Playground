package hr.fer.zemris.ml.lr.gd;


public class BatchGradientDescent {
	private final double e;
	private final double deltaStep;
	private double step;
	private boolean decreases = true;
	
	public BatchGradientDescent(double e, double deltaStep) {
		this.e = e;
		this.deltaStep = deltaStep;
		this.step = deltaStep;
	}
	
	private void changeStep(double previousError, double currentError) {
		if(decreases) {
			if(1 != step) {		
				if(previousError > currentError) {
					step += deltaStep;
				} else {
					decreases = false;
					step -= deltaStep;
				}
			}
		} 	
	}
	
	private double[] correct(double[] w, double[] deltaW) {
		for(int i = 0; i < w.length; ++i) {
			w[i] -= step * deltaW[i];
		}
		
		return w;
	}
	
	private double[] nextIteration(double[] w, IFunction fun) {
		double[] deltaW = new double[w.length];
		int N = fun.getN();
		for(int i = 0; i < N; ++i) {
			deltaW = fun.correction(deltaW, i, w);
		}
		w = correct(w, deltaW);
		
		return w;
	}
	
	public double[] lineSearch(double[] w, IFunction fun) {
		double error = fun.getError(w);
		double previousError;
		do {
			w = nextIteration(w, fun);
			previousError = error;
			error = fun.getError(w);
			changeStep(previousError, error);
			
			System.out.println(step);
		} while(Math.abs(previousError - error) < e);
		
		return w;
	}
}
