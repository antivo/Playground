package function;

public class F7 implements IFunction {
	private final int dim;
	
	
	
	
	public F7(int dim) {
		super();
		this.dim = dim;
	}

	@Override
	public void assertCoordLength(double... coord)
			throws IndexOutOfBoundsException {
		if(coord.length != dim) {
			throw new IndexOutOfBoundsException("F6 demands "+ dim + " arguments");
		}
	}

	@Override
	public double calculate(double... coord) throws IndexOutOfBoundsException {
		assertCoordLength(coord);
	
		double sum = 0;
		for(int i = 0; i < dim; ++i) {
			sum += Math.pow(coord[i], 2);
		}
		double value = Math.pow(sum, 0.25);
		
		double pedesetPuta = 50 * Math.pow(sum, 0.1);
		double onajDeugiDio = Math.pow(Math.sin(pedesetPuta), 2) + 1;
		
		value *= onajDeugiDio;
		
		return value;
	}

	@Override
	public int getDimm() {
		return dim;
	}
	

}
