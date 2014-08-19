package algorithm;

import java.util.Random;

import function.IFunction;

public class GeneticAlgorithm {
	final private int sizeOfPopulation;
	final private double pm;
	final private int iterations;
	
	private static Random rand = new Random(); 
	
	private static final double compareConstant =  1e-2;

	private static boolean isGreater(double d1, double d2) {
		if ((d1 - d2) > compareConstant) {
			return true;
		}
		return false;
	}
	
	public GeneticAlgorithm(int sizeOfPopulation, double pm, int iterations) {
		this.sizeOfPopulation = sizeOfPopulation;
		this.pm = pm;
		this.iterations = iterations;
	}
	
	public void findMin(IFunction fun) {
		// Pozeljno je pratiti vrijednost najboljeg clana populacije i ispisivati ga (prilikom promjene) tokom postupka. 
		Population pop = new Population(fun, sizeOfPopulation, pm, rand);
		double previousBest = pop.iterate().getFitness(fun);
		System.out.println(previousBest);
		for(int i = 1; i < iterations; ++i) {
			double best = pop.iterate().getFitness(fun);
			if(isGreater(previousBest, best)) {
				System.out.println(best);
			}
			previousBest = best;
		}
		
		pop.getBest().print();
	}
}
