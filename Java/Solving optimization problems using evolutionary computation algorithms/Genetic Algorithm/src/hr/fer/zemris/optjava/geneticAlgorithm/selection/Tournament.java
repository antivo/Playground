package hr.fer.zemris.optjava.geneticAlgorithm.selection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import hr.fer.zemris.optjava.geneticAlgorithm.operator.ICrossover;
import hr.fer.zemris.optjava.geneticAlgorithm.operator.IEvaluator;
import hr.fer.zemris.optjava.geneticAlgorithm.operator.IMutation;
import hr.fer.zemris.optjava.geneticAlgorithm.population.IPopulation;
import hr.fer.zemris.optjava.simulatedAnnealing.solution.DoubleArraySolution;

public class Tournament<T extends DoubleArraySolution> extends AbstractSelection<T> {
	private final int N;
	private final Random rand;

	private static void assertTournamentSize(int n) {
		if(n < 2) {
			throw new RuntimeException("no definition for Tournament:" + 2);
		}
	}
	
	private void assertSizeOfPopulation(IPopulation<T> population) {
		if(population.getSize() < N) {
			throw new RuntimeException("population to small for Tournament:" + N);
		}
	}
	
	public Tournament(Random rand, int n) {
		assertTournamentSize(n);
		this.N = n;
		this.rand = rand;
	}

	private List<T> makeChoices(IPopulation<T> population) {
		assertSizeOfPopulation(population);
		List<T> chosen = new ArrayList<T>();
		for(int i = 0; i < N; ++i) {
			chosen.add(population.getChromosome(rand.nextInt(population.getSize())));
		}
		
		return chosen;
	}
	
	@Override
	public T makeChromosome(IPopulation<T> population, IEvaluator<T> evaluator,
			ICrossover<T> crossover, IMutation<T> mutation) {
		List<T> chosen = makeChoices(population);
		Collections.sort(chosen);
		
		T fstParent = chosen.get(chosen.size() - 1);
		T sndParent = chosen.get(chosen.size() - 2);
		
		T child = crossover.makeChild(fstParent, sndParent);
		mutation.mutate(child);
		evaluator.evaluate(child);
		
		return child;
	}

	@Override
	public IPopulation<T> makePopulation(IPopulation<T> population,
			IEvaluator<T> evaluator, ICrossover<T> crossover,
			IMutation<T> mutation, boolean elitism) {
		
		IPopulation<T> newPopulation = population.newLikeThis();
		for(int i = 0; i < newPopulation.getSize(); ++i) {
			T child = makeChromosome(population, evaluator, crossover, mutation);
			newPopulation.add(child);
		}
		
		if(elitism) {
			elitism(population, newPopulation);
		}
		return newPopulation;
	}
	
}
