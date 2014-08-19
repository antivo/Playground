package f;

public interface IFunction {
	void assertCoordLength(double... coord) throws IndexOutOfBoundsException;
	double calculate(double ... coord) throws IndexOutOfBoundsException;
}
