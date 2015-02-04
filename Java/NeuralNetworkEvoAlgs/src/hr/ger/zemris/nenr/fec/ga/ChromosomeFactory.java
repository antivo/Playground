package hr.ger.zemris.nenr.fec.ga;

import hr.ger.zemris.nenr.fec.ga.chromosome.Chromosome;

import java.util.Random;

public class ChromosomeFactory {
	private final Random rand;
	private final DefinedEvaluator evaluator;
	private final int chromosomeLength;
	
	public ChromosomeFactory(Random rand, DefinedEvaluator evaluator, int chromosomeLength, double v1, double pcAdd, double pcReplace, double sigmaAdd, double sigmaReplace) {
		this.rand = rand;
		this.evaluator = evaluator;
		this.chromosomeLength = chromosomeLength;
		
		Chromosome.initializeChromosome(v1, pcAdd, pcReplace, sigmaAdd, sigmaReplace);
	}
	
	public Chromosome createRandomChromosome() {
		return new Chromosome(rand, chromosomeLength, evaluator);
	}
	
	public Chromosome createChild(Chromosome fst, Chromosome snd) {
		return new Chromosome(rand, fst, snd, evaluator);
	}
	
	
	
	
	
	
}
