package function;

public class F6 implements IFunction {
	private final int dim;
	
	public F6(int dim) {
		super();
		this.dim = dim;
	}

	@Override
	public void assertCoordLength(double... coord)
			throws IndexOutOfBoundsException {
		// TODO Auto-generated method stub
		if(coord.length != dim) {
			throw new IndexOutOfBoundsException("F6 demands "+ dim + " arguments");
		}
	}

	@Override
	public double calculate(double... coord) throws IndexOutOfBoundsException {
		assertCoordLength(coord);
		
		double value = 0.5;
		double sum = 0;
		for(int i = 0; i < dim; ++i) {
			sum += Math.pow(coord[i], 2);
		}
		
		double korijen = Math.sqrt(sum);
		double sinus = Math.sin(korijen);
		double b = Math.pow(sinus, 2) - 0.5;
		
		double n = 1 + 0.001 * sum;
		
		value += b/n;
		
		return value;
	}

	@Override
	public int getDimm() {
		return dim;
	}
	
}
