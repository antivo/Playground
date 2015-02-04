package hr.fer.zemris.nenr.anfis3.dataset;

public class Dataset {
	static private final int low = -4;
	static private final int high = 4;
	
	private double[] f;
	
	public Dataset() {
		int size = 81;
		this.f = new double[size];
		
		int idx = 0;
		for(int x = low; x <= high; ++x) {
			for(int y = low; y <= high; ++y) {
				this.f[idx++] = f(x, y);
			}
		}
	}
	
	public double get(int x, int y) throws IndexOutOfBoundsException {
		if(x >= low && x <= high) {
			if(y >= low && y <= high) {
				int idx = (x - low) * 9 + y - low;
				return f[idx];
			}
		}
		
		throw new IndexOutOfBoundsException();
	}
	
	static public int getSize() {
		return 81;
	}
	
	static public int getLow() {
		return low;
	}
	
	static public int getHigh() {
		return high;
	}
	
	static public double f(int x, int y) {
		int x_1 = x - 1;
		x_1 *= x_1;
		
		int y_2 = y + 2;
		y_2 *= y_2;
		
		int b1 = x_1 + y_2 - 5 * x * y + 3;
		double b2 = Math.cos(x/5);
		b2 *= b2;
		
		return b1*b2;
	}
}
