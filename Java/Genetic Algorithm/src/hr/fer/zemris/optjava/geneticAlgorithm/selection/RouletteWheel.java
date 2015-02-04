package hr.fer.zemris.optjava.geneticAlgorithm.selection;

import java.util.Random;

import hr.fer.zemris.optjava.geneticAlgorithm.operator.ICrossover;
import hr.fer.zemris.optjava.geneticAlgorithm.operator.IEvaluator;
import hr.fer.zemris.optjava.geneticAlgorithm.operator.IMutation;
import hr.fer.zemris.optjava.geneticAlgorithm.population.IPopulation;
import hr.fer.zemris.optjava.simulatedAnnealing.solution.SingleObjectiveSolution;

public class RouletteWheel<T extends SingleObjectiveSolution> extends AbstractSelection<T>{

	private final Random rand;
	
	public RouletteWheel(Random rand) {
		super();
		this.rand = rand;
	}

	private double calcTotalFitness(IPopulation<T> population) {
		double totalFitness = 0;
		double[] wheel = new double[population.getSize()];
		int idx = 0;
		for(T chromosome : population) {
			totalFitness += chromosome.fitness;
			wheel[idx++] = 0;
		}
		return totalFitness;
	}
	
	private T selectRandom(IPopulation<T> population, double totalFitness) {
		double selected = rand.nextDouble() * totalFitness;
		int idx = -1;
		int p = totalFitness > 0 ? 1 : -1;
		for(T chromosome : population) {
			++idx;
			selected -= chromosome.fitness;
			if(p * selected <= 0) {
				break;
			}
		}
		return population.getChromosome(idx);
	}
	
	public T makeChromosome(IPopulation<T> population, IEvaluator<T> evaluator,
			ICrossover<T> crossover, IMutation<T> mutation, double totalFitness) {
		T fstParent = selectRandom(population, totalFitness);
		T sndParent = selectRandom(population, totalFitness);
		
		T child = crossover.makeChild(fstParent, sndParent);
		mutation.mutate(child);
		evaluator.evaluate(child);
		
		return child;
	}
	
	@Override
	public T makeChromosome(IPopulation<T> population, IEvaluator<T> evaluator,
			ICrossover<T> crossover, IMutation<T> mutation) {
		double totalFitness = calcTotalFitness(population);
		return makeChromosome(population, evaluator, crossover, mutation, totalFitness);
	}

	@Override
	public IPopulation<T> makePopulation(IPopulation<T> population,
			IEvaluator<T> evaluator, ICrossover<T> crossover,
			IMutation<T> mutation, boolean elitism) {
		
		double totalFitness = calcTotalFitness(population);
		IPopulation<T> newPopulation = population.newLikeThis();
		for(int i = 0; i < newPopulation.getSize(); ++i) {
			T child = makeChromosome(population, evaluator, crossover, mutation, totalFitness);
			newPopulation.add(child);
		}
		
		if(elitism) {
			elitism(population, newPopulation);
		}
		return newPopulation;
	}
}
