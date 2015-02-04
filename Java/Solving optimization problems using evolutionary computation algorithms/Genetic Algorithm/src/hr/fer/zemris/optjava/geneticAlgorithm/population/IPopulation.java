package hr.fer.zemris.optjava.geneticAlgorithm.population;

import hr.fer.zemris.optjava.simulatedAnnealing.solution.SingleObjectiveSolution;

public interface IPopulation<T extends SingleObjectiveSolution> extends Iterable<T>{
	int getSize();
	
	T getChromosome(int idx);
	void setChromosome(T chromosome, int idx);
	void add(T chromosome);
	
	T getBest();
	
	IPopulation<T> newLikeThis();
}
