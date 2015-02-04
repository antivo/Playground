package DifferentialEvolution;

public class Individual {
	double weights[];
	
	public double[] get(){
		return weights;
	}
	
	double distance;
	
	Individual(int dimensions) {
		this.weights = new double[dimensions];
	}
}
