package hr.fer.zemris.optjava.simulatedAnnealing.tempSchedule;

public class GeometricTempSchedule implements ITempSchedule {

	private double alpha;
	private double tCurrent;
	private int innerLimit;
	private int outerLimit;
	
	private boolean frozen = false;
	
	private int innerLoopCounter;
	private int outerLoopCounter;

	
	public GeometricTempSchedule(double alpha, double tInitial, int innerLimit,
			int outerLimit) {
		this.alpha = alpha;
		this.innerLimit = innerLimit;
		this.outerLimit = outerLimit;
		
		tCurrent = tInitial;
		
		innerLoopCounter = 0;
		outerLoopCounter = 0;
	}

	@Override
	public double getNextTemperature() {
		double ret = 0;
		if(outerLoopCounter < outerLimit) {
			ret = tCurrent;
			++innerLoopCounter;
			if(innerLoopCounter >= innerLimit) {
				innerLoopCounter = 0;
				tCurrent *= alpha;
				++outerLoopCounter;
			}
		} else {
			frozen = true;
		}
		return ret;
	}

	@Override
	public int getInnerLoopCounter() {
		return innerLoopCounter;
	}

	@Override
	public int getOuterLoopCounter() {
		return outerLimit;
	}
	
	public static double calculateAlpha(int desiredOuterIterations, double tInitial, double tFinal) {
		if(0 == tInitial) {
			tInitial = 1e-12;
		}
		double q = tFinal / tInitial;
		double e = 1 / (double) (desiredOuterIterations - 1);
		return Math.pow(q, e);
	}

	@Override
	public boolean isFrozen() {
		return frozen;
	}

}
