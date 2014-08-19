package DifferentialEvolution;

import hr.ger.zemris.nenr.fec.ga.DefinedEvaluator;

import java.util.Random;

public class DiffEvo {
	
	//evaluator afiniteta
	private DefinedEvaluator evaluator;
	
	// Mimimumi varijabli u prostoru pretraživanja
	private double[] varMin;
	
	// Maksimumi varijabli u prostoru pretraživanja
	private double[] varMax;
	
	// Generator sluèajnih brojeva
	private Random rand;
	
	// Broj dimenzija funkcije
	private int dims;
	
	private Individual[] population;
	 
	private int populationSize;

	private double recombinationProbability;

	private double factorF;
	
	public DiffEvo(DefinedEvaluator simulator, int dimenzijaProstora){
		this.evaluator = simulator;

		
		this.dims = dimenzijaProstora; 
		this.populationSize = 50;
		this.recombinationProbability = 0.1; // e[0,1]
		this.factorF = 0.7; // e[0,2]
		
		this.varMin = new double[dims];
		this.varMax = new double[dims];
		for(int d = 0; d < dims; d++) {
			varMin[d] = -1.0;
			varMax[d] = 1.0;
		}
		
		rand = new Random();
		population = new Individual[populationSize];
		
		for(int i = 0; i < populationSize; i++) {
			population[i] = new Individual(dims);
			for(int d = 0; d < dims; d++) 
					population[i].weights[d] = rand.nextGaussian()*(varMax[d]-varMin[d])+varMin[d];
		}

		evaluiraj(this.population);
	}
	
	private void evaluiraj(Individual[] pop) {
		double dis;
		for(int i = 0; i < pop.length; ++i) {
			dis = evaluator.calcError(pop[i].weights);
			pop[i].distance = dis;
		}
	}
	
	private void evaluirajJedinku(Individual ind) {
		double dis;
			dis = evaluator.calcError(ind.weights);
			ind.distance = dis;
	}
	
	//koristi se za odabrat tri indeksa uz jedan dani, od ta cetri nijedan ne smije bit jednak onome drugome
	 private int getRandomIndex(int r1, int r2, int r3) {
         int randInx = rand.nextInt(populationSize);

         while (randInx == r1 || randInx == r2 || randInx == r3) 
        	 randInx = rand.nextInt(populationSize);
         
         return randInx;
	 }
	
	private Individual mutation(int currentIdx) {
		 int targetIndividualIndex = getRandomIndex(currentIdx, -1, -1);
         int individualAIndex = getRandomIndex(currentIdx, targetIndividualIndex, -1);
         int individualBIndex = getRandomIndex(currentIdx, targetIndividualIndex, individualAIndex);

         Individual experimentalIndividual = new Individual(dims);
         double position;

         for (int d = 0; d < dims; d++) {
                 position = population[targetIndividualIndex].weights[d] + factorF * (population[individualAIndex].weights[d] - population[individualBIndex].weights[d]);
                 if (position > this.varMax[d])
                	 experimentalIndividual.weights[d] = this.varMax[d];
                 else if (position < this.varMin[d])
                	 experimentalIndividual.weights[d] = this.varMin[d];
                 else 
                	 experimentalIndividual.weights[d] = position;
         }
         this.evaluirajJedinku(experimentalIndividual);

         return experimentalIndividual;
	}
	
	private Individual crossover(Individual currentIndividual, Individual experimentalIndividual) {
		Individual crx = new Individual(dims);
        for (int d = 0; d < dims; d++)
        	if (rand.nextDouble() <= recombinationProbability) 
        		crx.weights[d] = experimentalIndividual.weights[d];
        	else
        		crx.weights[d] = currentIndividual.weights[d];
        
        evaluirajJedinku(crx);
        return crx;
	}
	
	private void iterate() {
		 Individual experimentalIndividual;
		 Individual trial;
		 
		 for (int i = 0; i < populationSize; i++) {
			  
			  experimentalIndividual = mutation(i);
			  trial = crossover(population[i],experimentalIndividual);
			  if (trial.distance < population[i].distance)
				  population[i] = trial;
			  //System.out.printf("%f \n", population[i].distance);
		 }
		 //System.out.printf("%f \n", population[0].distance);
		 //evaluiraj(this.population);
	}

	public Individual train() {
		int maximumIterations = 1000;
		for (int i = 0; i < maximumIterations; i++) {
			 iterate();
		}
		
		Individual ofInterest = population[0];
		for(int i = 1; i < this.populationSize; ++i)
			if (population[i].distance < ofInterest.distance )
				ofInterest = population[i];
		
		return ofInterest;
	}
}
