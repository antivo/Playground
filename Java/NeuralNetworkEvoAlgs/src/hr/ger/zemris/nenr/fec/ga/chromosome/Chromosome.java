package hr.ger.zemris.nenr.fec.ga.chromosome;

import hr.ger.zemris.nenr.fec.ga.DefinedEvaluator;

import java.util.Random;

public class Chromosome {
	private static boolean initialized = false;
	
	private static void assertInitialized() {
		if(!initialized) {
			throw new IllegalAccessError("Cannot use Chromosome class before using Chromosome.initializeChromosome");
		}
	}
	
	private static double _v1;
	private static double _pcAdd;
	private static double _pcReplace;
	private static double _sigmaAdd;
	private static double _sigmaReplace;
	
	public static void initializeChromosome(double v1, double pcAdd, double pcReplace, double sigmaAdd, double sigmaReplace) {
		_v1 = v1;
		_pcAdd = pcAdd;
		_pcReplace = pcReplace;
		_sigmaAdd = sigmaAdd;
		_sigmaReplace = sigmaReplace;
		
		initialized = true;
	}
	
	private final double[] genes;
	private final double error;
		
	public void print() {
		StringBuilder sb = new StringBuilder();
		sb.append("ERROR= ").append(error).append(" GENES: ");
		for(int i = 0; i < genes.length; ++i) {
			String out = String.format("%.2f", genes[i]);
			sb.append(out).append(" ");
		}
		System.out.println(sb.toString());
	}
	
	public double[] getGenes() {
		return genes;
	}
	
	public double getError() {
		return error;
	}
	
	public Chromosome(Random rand, int dim, DefinedEvaluator evaluator) {		
		assertInitialized();
		
		genes = new double[dim];
		for(int i = 0; i < dim; ++i) {
			genes[i] = nextGaussian(rand, rand.nextDouble());
		}
		error = evaluator.calcError(genes);
	}
	
	public Chromosome(Random rand, Chromosome fst, Chromosome snd, DefinedEvaluator evaluator) {
		assertInitialized();
		
		double newGenes[] = crossGenes(rand, fst, snd);
		newGenes = mutate(rand, newGenes);
		
		this.genes = newGenes;
		error = evaluator.calcError(genes);
	}
	
	
	private static double[] mutate(Random rand, double[] genes) {
		double choiceOfMutation = rand.nextDouble();
		if(choiceOfMutation <= _v1) {
			return mutateAdd(rand, genes, _pcAdd, _sigmaAdd);
		} else {
			return mutateReplace(rand, genes, _pcReplace, _sigmaReplace);
		}
	}
	
	private static double[] crossGenes(Random rand, Chromosome fst, Chromosome snd) {
		int choice = rand.nextInt(3);
		switch(choice) {
		case 0: return crossOnePoint(rand, fst, snd);
		case 1: return crossUniform(fst, snd);
		case 2: return crossTwoPoint(rand, fst, snd);
		
		default: return crossUniform(fst, snd);
		}
	}
	
	private static double[] crossUniform(Chromosome fst, Chromosome snd) {
		int genesLength = fst.genes.length;
		double[] newGenes = new double[genesLength];
		for(int i = 0; i < newGenes.length; ++i) {
			newGenes[i] = fst.genes[i] + snd.genes[i];
		}
		
		return newGenes;
	}
	
	private static double[] crossOnePoint(Random rand, Chromosome fst, Chromosome snd) {
		int genesLength = fst.genes.length;
		double[] newGenes = new double[genesLength];
		
		int breakPoint = rand.nextInt(genesLength);
		for(int i = 0; i < breakPoint; ++i) {
			newGenes[i] = fst.genes[i];
		}
		
		for(int i = breakPoint; i < genesLength; ++i) {
			newGenes[i] = snd.genes[i];
		}
		
		return newGenes;
	}
	
	private static double[] crossTwoPoint(Random rand, Chromosome fst, Chromosome snd) {
		int genesLength = fst.genes.length;
		double[] newGenes = new double[genesLength];
		
		int firstBreakPoint = rand.nextInt(genesLength);
		int secondBreakPoint = rand.nextInt(genesLength);
		if(secondBreakPoint < firstBreakPoint) {
			int temp = firstBreakPoint;
			firstBreakPoint = secondBreakPoint;
			secondBreakPoint = temp;
		}
		
		for(int i = 0; i < firstBreakPoint; ++i) {
			newGenes[i] = fst.genes[i];
		}
		
		for(int i = firstBreakPoint; i < secondBreakPoint; ++i) {
			newGenes[i] = snd.genes[i];
		}
		
		for(int i = secondBreakPoint; i < genesLength; ++i) {
			newGenes[i] = fst.genes[i];
		}
		
		return newGenes;
	}
	
	private static double[] mutateAdd(Random rand, double[] genes, double pc, double sigma) {
		for(int i = 0; i < genes.length; ++i) {
			double probabilityOfChoice = rand.nextDouble();
			if(probabilityOfChoice < pc) {
				double add = nextGaussian(rand, sigma);
				genes[i] += add;
			}
		}
		return genes;
	}
	
	private static double[] mutateReplace(Random rand, double[] genes, double pc, double sigma) {
		for(int i = 0; i < genes.length; ++i) {
			double probabilityOfChoice = rand.nextDouble();
			if(probabilityOfChoice < pc) {
				double replace = nextGaussian(rand, sigma);
				genes[i] = replace;
			}
		}
		return genes;
	}
	
	private static double nextGaussian(Random rand, double sigma) {
		return (rand.nextGaussian() - 0.5) * sigma * 2;
	}
}
