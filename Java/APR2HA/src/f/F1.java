package f;

public class F1 implements IFunction {
	@Override
	public double calculate(double... coord) throws IndexOutOfBoundsException {
		assertCoordLength(coord);
		
		double x = coord[0];
		double y = coord[1];
		
		double x2 = Math.pow(x,2);
		double x2my = x2 - y; 
		
		double retValue = 10 * Math.pow(x2my, 2) + Math.pow(1 - x, 2);
			
		return retValue;
	}

	@Override
	public void assertCoordLength(double... coord)
			throws IndexOutOfBoundsException {
		if(coord.length != 2) {
			throw new IndexOutOfBoundsException("F1 has 2 arguments");
		}
	}
}
