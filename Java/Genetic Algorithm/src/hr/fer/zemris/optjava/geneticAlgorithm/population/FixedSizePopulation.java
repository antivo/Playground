package hr.fer.zemris.optjava.geneticAlgorithm.population;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import hr.fer.zemris.optjava.simulatedAnnealing.solution.SingleObjectiveSolution;

public class FixedSizePopulation<T extends SingleObjectiveSolution> implements IPopulation<T> {
	private final List<T> population;
	private final int size;
	
	public FixedSizePopulation(int size) {
		this.size = size;
		this.population = new ArrayList<T>(size);
	}
	
	@Override
	public Iterator<T> iterator() {
		return population.iterator();
	}

	@Override
	public int getSize() {
		return size;
	}

	@Override
	public T getChromosome(int index) {
		return population.get(index);
	}

	@Override
	public void setChromosome(T chromosome, int index) {
		population.set(index, chromosome);
	}

	@Override
	public void add(T chromosome) {
		population.add(chromosome);
	}

	@Override
	public T getBest() {
		T best = population.get(0);
		for(T solution : population) {
			if (solution.compareTo(best) > 0) {
				best = solution;
			}
		}
		return best;
	}

	@Override
	public IPopulation<T> newLikeThis() {
		return new FixedSizePopulation<T>(population.size());
	}

}
