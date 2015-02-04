package f;

public class F4 implements IFunction {

	@Override
	public void assertCoordLength(double... coord)
			throws IndexOutOfBoundsException {
		if(coord.length != 2) {
			throw new IndexOutOfBoundsException("F4 demands 2 arguments");
		}
		
	}

	@Override
	public double calculate(double... coord) throws IndexOutOfBoundsException {
		assertCoordLength(coord);
		
		double x = coord[0];
		double y = coord[1];
		
		double retValue = Math.abs((x-y) * (x+y)) + Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
		return retValue;
	}
	
}
