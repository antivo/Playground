package hr.fer.zemris.ml.lr.gd;

public interface IFunction {
	public int getN();
	public double[] correction(double[] deltaW, int entryIndex, double[] w);
	public double getError(double[] w);
}
