package function;

public class F3 implements IFunction {
	private final double[] parameters;
	
	public F3(double... par) throws Exception {
		this.parameters = par;
		
		if(par.length != 5) {
			throw new Exception("F3 demands 5 parameters p1 p2 p3 p4 p5");
		}
	}
	
	
	@Override
	public void assertCoordLength(double... coord)
			throws IndexOutOfBoundsException {
		if(coord.length != 5) {
			throw new IndexOutOfBoundsException("F3 has 5 arguments");
		}
	}

	@Override
	public double calculate(double... coord) throws IndexOutOfBoundsException {
		assertCoordLength(coord);
		
		double retValue = 0;
		for(int i = 0; i < 5; ++i) {
			double x = coord[i];
			double p = parameters[i];
			retValue += Math.pow(x - p, 2); 
		}
		
		return retValue;
	}


	@Override
	public int getDimm() {
		return 5;
	}
	

}
