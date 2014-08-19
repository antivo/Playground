package ParticleSwarmOptimization;

import hr.ger.zemris.nenr.fec.ga.DefinedEvaluator;

import java.util.Random;

public class PSO {
	//simulator
	private final DefinedEvaluator evaluator;
	
	// Mimimumi varijabli u prostoru pretraživanja
	private double[] varMin;
	
	// Maksimumi varijabli u prostoru pretraživanja
	private double[] varMax;
	
	// Maksmalna promjena varijable odjednom, za svaku dimenziju
	private double[] velBounds;
	
	// Postotak raspona prostora pretraživanja koji se koristi za
	// izr aèun ogranièenja pomaka u jednom koraku
	private double velBoundsPercentage;
	
	// Globalni brojaè iteracija
	private int iteracija;
	
	// Težina koju korisimo na poèetku
	private double linWeightStart;
	
	// Težina koju korisimo na kraju smanjivanja
	private double linWeightEnd;
	
	//Iteracija u kojoj tezina pada na kraju i dalje se ne mijenja
	private int linWeightTreshold;
	
	///Broj cestica s kojima radimo
	private int VEL_POP;
	
	// Generator sluèajnih brojeva
	private Random rand;
	
	// Broj dimenzija funkcije
	private int dims;
	
	// Konstanta c1
	private double c1;
	
	// Konstanta c2
	private double c2;
	
	// Èestice
	private Particle[] particles;
	
	// Susjedstvo
	private LocalNeighborhood neighborhood;
	
	public PSO(DefinedEvaluator evaluator, int dimenzijaProstora) {
		this.evaluator = evaluator;
		
		this.VEL_POP = 20;
		this.dims = dimenzijaProstora;
		this.c1 = 2.5;
		this.c2 = 2.05; 
		
		this.iteracija = 0;
		this.linWeightStart = 0.9;
		this.linWeightEnd = 0.4;
		this.linWeightTreshold = 2;
		
		// Definiranje minimalnih i maksimalnih vrijednosti po dimenzijama,
		// te definiranje maksimalne promjene varijable u jednom koraku.
		this.varMin = new double[dims];
		this.varMax = new double[dims];
		this.velBounds = new double[dims];
		
		velBoundsPercentage = 0.10; // dozvoli pomak od 10% raspona
		for(int d = 0; d < dims; d++) {
			varMin[d] = -0.5;
			varMax[d] = 0.5;
			velBounds[d] = (varMax[d]-varMin[d])*velBoundsPercentage;
		}
		
		// Generator sluèajnih brojeva.
		rand = new Random();
		
		// Inicijalizacija
		particles = new Particle[VEL_POP];
		
		for(int i = 0; i < VEL_POP; i++) {
			particles[i] = new Particle(dims);
			for(int d = 0; d < dims; d++) {
					particles[i].weights[d] = rand.nextDouble()*(varMax[d]-varMin[d])+varMin[d];
					particles[i].oldWeights[d] = particles[i].weights[d];
					particles[i].bestWeights[d] = particles[i].weights[d];
					particles[i].velocity[d] = rand.nextDouble() * (2*velBounds[d])-velBounds[d];
			}
			
			double[] w = particles[i].weights; // uvjek prije calculate
			double bValue = evaluator.calcError(w);
			particles[i].bestValue = bValue;
		}
		
		//Vel susjedstva 5 ?... pa nek bude
		this.neighborhood = new LocalNeighborhood(VEL_POP, dims, 4);
	}
	
	private void nextIteration() {
		++iteracija;
		
		double w;
		if(iteracija > linWeightTreshold) 
			w = linWeightEnd;
		else 
			w = linWeightStart + (linWeightEnd - linWeightStart) * (iteracija - 1.0) / linWeightTreshold;
		
		//Moras pozvat scan da za update podataka o tome sto se dogadja u susjedstvu
		neighborhood.scan(particles);
		
		//azuriraj brzinu i poziciju svake cestice
		for(int i = 0; i < particles.length; ++i) {
			double socialBest[] = neighborhood.findBest(i);
			for(int d = 0; d < dims; d++) {
				particles[i].oldWeights[d] = particles[i].weights[d];
				particles[i].velocity[d] =  w * particles[i].velocity[d] 
				                         + c1 * rand.nextDouble()*(particles[i].bestWeights[d] - particles[i].weights[d])
				                         + c2 * rand.nextDouble()*(socialBest[d] - particles[i].weights[d]);
				if (particles[i].velocity[d] < -velBounds[d]) {
					particles[i].velocity[d] = -velBounds[d];
				}
				else if (particles[i].velocity[d] > velBounds[d]) {
					particles[i].velocity[d] = velBounds[d];
				}
				particles[i].weights[d] = particles[i].weights[d] + particles[i].velocity[d];
			}
		}
		
		//postavi vrijednost "funkcije greske" i azuriraj najbolje rjesenje cestice
		for(int i = 0; i < particles.length; i++) {
			double newValue = evaluator.calcError(particles[i].weights);
			
			particles[i].value = newValue;
			
			if(particles[i].value < particles[i].bestValue) {
				particles[i].bestValue = particles[i].value;
				for(int d = 0; d < dims; d++)
					particles[i].bestWeights[d] = particles[i].weights[d];
			}
		}
	}
	
	public Particle train() {
		//Ponavljaj zadani broj puta
		for(int iter = 0; iter < 12000; ++iter) {
			nextIteration();
		}

		int bestindex = 0;
		double bestValue = particles[bestindex].bestValue;
		
		for(int i = 1; i < particles.length; i++) 
			if(particles[i].bestValue < bestValue) {
				bestValue = particles[i].bestValue;
				bestindex = i;
			}
		
		return particles[bestindex];
	}
}