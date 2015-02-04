package ParticleSwarmOptimization;

public class Particle {
	// Najbolje rje�enje
	double[] bestWeights;
	
	// Dobrota najboljeg rje�enja
	double bestValue;
	
	// Prethodno rje�enje
	double[] oldWeights;
	
	// Trenutno rje�enje
	double[] weights;
	
	// Vektor brzine
	double[] velocity;
	
	// Vrijednost funkcije u trenutnom rje�enju
	double value;
	/**
	* Konstruktor �estice. Prima broj dimenzija.
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