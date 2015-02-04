package ClonalSelectionAlgorithm;

import hr.ger.zemris.nenr.fec.ga.DefinedEvaluator;

import java.util.Random;
import java.util.Arrays;

public class ClonAlg {
	
	//evaluator afiniteta
	private final DefinedEvaluator evaluatorAfiniteta;
	
	//velicina populacije klonova
	private int paramBeta;
	
	//broj antitijela koje cemo dodavati
	private int paramD;
	
	//broj antitijela populacije
	private int paramN;
	
	// Mimimumi varijabli u prostoru pretraživanja
	private double[] varMin;
	
	// Maksimumi varijabli u prostoru pretraživanja
	private double[] varMax;
	
	// Generator sluèajnih brojeva
	private Random rand;
	
	// Broj dimenzija funkcije
	private int dims;

	//polacija
	private Antibody[] population;
	
	//polacija klonova
	private Antibody[] clonedPopulation;
	
	// Velièina populacije klonova
	private int clonedPopulationSize;
	
	//rangovi klonirane populacije
	private int[] clonedPopulationRanks;
	
	public ClonAlg(DefinedEvaluator simulator, int dimenzijaProstora) {
		this.evaluatorAfiniteta = simulator;

		
		this.dims = dimenzijaProstora; 
		this.paramN = 100;
		this.paramD = 10;
		this.paramBeta = 10;
		
		this.varMin = new double[dims];
		this.varMax = new double[dims];
		for(int d = 0; d < dims; d++) {
			varMin[d] = -1.0;
			varMax[d] = 1.0;
		}
		
		rand = new Random();
		population = new Antibody[paramN];
		
		for(int i = 0; i < paramN; i++) {
			population[i] = new Antibody(dims);
			for(int d = 0; d < dims; d++) 
					population[i].weights[d] = rand.nextGaussian()*(varMax[d]-varMin[d])+varMin[d];
			
		}
		
		this.clonedPopulationSize = 0;
		for(int i = 1; i <= paramN; i++) {
			clonedPopulationSize += (int)((paramBeta*paramN)/((double)i)+0.5);
		}
		
		clonedPopulation = new Antibody[clonedPopulationSize];
		clonedPopulationRanks = new int[clonedPopulationSize];
	}
	
	public Antibody train() {
		
		int iter = 0;
		int iterLimit = 200;
		// Ponavljaj zadani broj puta
		while(iter < iterLimit) {
			iter++;
	
			{//Evaluiraj -> postavi population[i].affinity
				double aff;
				for(int i = 0; i < population.length; ++i) {
					aff = evaluatorAfiniteta.calcError(population[i].weights);
					population[i].afinity = aff;
				}
			}
			cloning();
			hyperMutation();
			{//Evaluiraj -> postavi clonedPopulation[i].affinity

				double aff;
				for(int i = 0; i < clonedPopulation.length; ++i) {
					aff = evaluatorAfiniteta.calcError(clonedPopulation[i].weights);
					clonedPopulation[i].afinity = aff;
				}
			}
			//for(int i= 0; i < this.clonedPopulation.length; ++i)
			//	System.out.printf("%f \n", clonedPopulation[0].afinity);
			
			select();
			birthAndReplace();
		}
		
		return population[0];
	}
	
	public void cloning() {
		Arrays.sort(population);
		int index = 0;
		for(int i = 1; i <= population.length; i++) {
			Antibody solution = population[i-1];
			int copies = (int)((paramBeta*paramN)/((double)i)+0.5);
			for(int j = 0; j < copies; j++) {
				Antibody copy = new Antibody(dims);
				//kopiraj
					for(int d = 0; d < dims; ++d)
						copy.weights[d] = solution.weights[d];
					copy.afinity = solution.afinity;
				
				clonedPopulation[index] = copy;
				clonedPopulationRanks[index] = i;
				index++;
			}
		}
	}
	public void hyperMutation() {
		// za zadnju jedinku zelimo 1 + Antibody.weights.length / 2 mutacija => tau = 1.442 * (population.length-1)
		// prvo rjesenje zelimo ostaviti netaknuto stoga krecemo od 1
		double tau = 1.442 * (population.length-1);
		
		for(int index = 1; index < clonedPopulation.length; index++) {
			Antibody clone = clonedPopulation[index];
			int rank = clonedPopulationRanks[index]-1;
			
			int mutations = (int)(1 + clone.weights.length*0.25*(1-Math.exp(-rank/tau)) + 0.5);
			for(int attempt = 0; attempt < mutations; ++attempt) {
				int pozMut = rand.nextInt(dims);
				clone.weights[pozMut] = rand.nextGaussian()*(varMax[pozMut]-varMin[pozMut])+varMin[pozMut];
			}
		}
	}
	
	public void select() {
		Arrays.sort(clonedPopulation);
		for(int i = 0; i < population.length; i++) 
			population[i] = clonedPopulation[i];
	}
	
	private void birthAndReplace() {
		int offset = population.length-paramD;
		for(int i=0; i < paramD; i++) {
			population[offset + i] = new Antibody(dims);
			for(int d = 0; d < dims; d++) 
					population[offset + i].weights[d] = rand.nextGaussian()*(varMax[d]-varMin[d])+varMin[d];
		}
	}
}
