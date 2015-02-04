package ClonalSelectionAlgorithm;

public class Antibody implements Comparable<Antibody>{
	double weights[];
	
	public double[] get() {
		return weights;
	}
	
	double afinity;
	
	Antibody(int dimensions) {
		this.weights = new double[dimensions];
	}
	
	// uzlazno sortira
	public int compareTo(Antibody o) {
		if(this.afinity < o.afinity) {
			return -1;
		}
		if(this.afinity > o.afinity) {
			return 1;
		} 
		return 0;
		}
}
