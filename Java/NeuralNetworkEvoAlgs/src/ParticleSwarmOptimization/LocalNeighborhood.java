package ParticleSwarmOptimization;

public class LocalNeighborhood {
	//Broj cestica
	int particlesCount;
	
	//Dimenzija prostora koji se pretrazuje
	int dims;
	
	// Velièina susjedstva
	int nSize;
	
	// Najbolja rješenje za susjedstvo svake cestice
	double[][] best;
	
	public LocalNeighborhood(int particlesCount, int dims, int nSize) {
		this.particlesCount = particlesCount;
		this.dims = dims;
		this.nSize = nSize;
		best = new double[particlesCount][dims];
	}
	
	//Pronalazi najbolja rješenja susjedstva za sve jedinke
	public void scan(Particle[] particles) {
		for(int index = 0; index < particles.length; index++) {
			int startFrom = index - nSize/2;
			int endAt = index + nSize/2;
			if(startFrom < 0) 
			  startFrom = 0;
			if(endAt >= particles.length) 
				endAt = particles.length-1;
			
			int bestindex = startFrom;
			double bestValue = particles[bestindex].bestValue;
			for(int i = startFrom + 1; i <= endAt; i++) {
				if(particles[i].bestValue < bestValue) {
					bestValue = particles[i].bestValue;
					bestindex = i;
				}
			} 
			for(int d = 0; d < dims; d++) {
				best[index][d] = particles[bestindex].bestWeights[d]; // najzanimljiviji redak od svega
			}
		}
	}
	
	//Vraæa najbolje rješenje za zadanu èesticu.
	public double[] findBest(int index) {
		return best[index];
	}
}