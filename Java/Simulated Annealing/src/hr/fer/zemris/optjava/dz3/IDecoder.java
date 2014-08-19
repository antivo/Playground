package hr.fer.zemris.optjava.dz3;

public interface IDecoder<T> {
	double[] decode(T code);
	void decode(T code, double result[]);
}
