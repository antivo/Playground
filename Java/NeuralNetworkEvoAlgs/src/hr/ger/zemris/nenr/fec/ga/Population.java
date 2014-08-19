package hr.ger.zemris.nenr.fec.ga;

import hr.ger.zemris.nenr.fec.ga.chromosome.Chromosome;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class Population {
	private final Random rand;
	private final ChromosomeFactory chromosomeFactory;
	private Chromosome[] population;
	private Chromosome minimum;
	
	public Chromosome[] getPopulation() {
		return population;
	}

	public Chromosome getMinimum() {
		return minimum;
	}

	public Population(Random rand, int sizeOfPopulation, int chromosomeDimension, DefinedEvaluator evaluator, double v1, double pcAdd, double pcReplace, double sigmaAdd, double sigmaReplace) {
		this.rand = rand;
		this.chromosomeFactory = new ChromosomeFactory(rand, evaluator, chromosomeDimension, v1, pcAdd, pcReplace, sigmaAdd, sigmaReplace);
		this.population = new Chromosome[sizeOfPopulation];
		
		for(int i = 0; i < sizeOfPopulation; ++i) {
			this.population[i] = this.chromosomeFactory.createRandomChromosome();
		}
		
		sortPopulation();
		this.minimum = population[0];
	}
	
	private void sortPopulation() {
		Arrays.sort(population, new Comparator<Chromosome>() {
		   public int compare(Chromosome c1, Chromosome c2) {
			  double e1 = c1.getError();
			  double e2 = c2.getError();
			  
		      if (e1 > e2) {
		    	  return +1;
		      } else if (e1 < e2) {
		    	  return -1;
		      } else {
		    	  return 0;
		      }
		   }
		});
	}
	
	private Chromosome createNewChromosome(double sumError) {
		int idxFst = rouletteWheelSelection(sumError, -1);
		int idxSnd = rouletteWheelSelection(sumError, idxFst);
		
		Chromosome fst = population[idxFst];
		Chromosome snd = population[idxSnd];
		
		Chromosome child = chromosomeFactory.createChild(fst, snd);
		
		return child;
	}
	
	private void setPopulation(Chromosome[] population) {
		this.population = population;
		sortPopulation();
		minimum = population[0];
	}

	public double getErrorSum() {
		double sumError = 0;
		for(int i = 0; i < population.length; ++i) {
			sumError += population[i].getError();
		}
		
		return sumError;
	}
	
	// vrati najbolje rjesenje
	public Chromosome nextGeneration(Random rand, DefinedEvaluator evaluator) {
		int sizeOfPopulation = population.length;
		Chromosome[] newPopulation = new Chromosome[sizeOfPopulation];
		
		newPopulation[0] = population[0];
		newPopulation[1] = population[1];
		
		double sumError = getErrorSum();
		for(int i = 2; i < sizeOfPopulation; ++i) {
			newPopulation[i] = createNewChromosome(sumError);
		}
		
		setPopulation(newPopulation);
		return minimum;
	}
	
	
	private int rouletteWheelSelection(double sumError, int forbiden) {
		int index = population.length - 1;;
		do {
			double selected = Math.abs(rand.nextGaussian()) * population.length / 4;
			if(selected > population.length - 1) {
				continue;
			}
			index = (int) Math.floor(selected);
			
		} while(index == forbiden);
		return index;
	}
}
