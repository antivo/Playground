package hr.fer.zemris.optjava.simulatedAnnealing.tempSchedule;


public interface ITempSchedule {
	public double getNextTemperature();
	public int getInnerLoopCounter();
	public int getOuterLoopCounter();
	public boolean isFrozen();
}
