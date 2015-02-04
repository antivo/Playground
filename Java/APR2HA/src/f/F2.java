package f;

public class F2 implements IFunction {

	@Override
	public void assertCoordLength(double... coord)
			throws IndexOutOfBoundsException {
		if(coord.length != 2) {
			throw new IndexOutOfBoundsException("F2 has 2 arguments");
		}
		
	}

	@Override
	public double calculate(double... coord) throws IndexOutOfBoundsException {
		assertCoordLength(coord);
		
		double x = coord[0];
		double y = coord[1];
		
		double retValue = Math.pow(x - 4, 2) + 4 * Math.pow(y-2, 2);
		
		return retValue;
	}

}
