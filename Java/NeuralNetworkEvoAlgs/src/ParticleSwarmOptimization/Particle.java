package ParticleSwarmOptimization;

public class Particle {
	// Najbolje rješenje
	double[] bestWeights;
	
	// Dobrota najboljeg rješenja
	double bestValue;
	
	// Prethodno rješenje
	double[] oldWeights;
	
	// Trenutno rješenje
	double[] weights;
	
	// Vektor brzine
	double[] velocity;
	
	// Vrijednost funkcije u trenutnom rješenju
	double value;
	/**
	* Konstruktor èestice. Prima broj dimenzija.
	*
	* @param dimensions broj dimenzija prostora
	*/
	public Particle(int dimensions) {
		this.weights = new double[dimensions];
		this.oldWeights = new double[dimensions];
		this.velocity = new double[dimensions];
		this.bestWeights = new double[dimensions];
	}
	
	public double[] get() {
		return weights;
	}
}