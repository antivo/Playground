package hr.fer.zemris.optjava.geneticAlgorithm;

import java.io.IOException;
import java.util.Random;

import Jama.Matrix;
import hr.fer.zemirs.optjava.numeric.Parser;
import hr.fer.zemirs.optjava.numeric.function.IFunction;
import hr.fer.zemris.optjava.geneticAlgorithm.operator.DoubleArraySolutionCrossover;
import hr.fer.zemris.optjava.geneticAlgorithm.operator.DoubleArraySolutionEvaluator;
import hr.fer.zemris.optjava.geneticAlgorithm.operator.DoubleArraySolutionMutation;
import hr.fer.zemris.optjava.geneticAlgorithm.operator.ICrossover;
import hr.fer.zemris.optjava.geneticAlgorithm.operator.IEvaluator;
import hr.fer.zemris.optjava.geneticAlgorithm.operator.IMutation;
import hr.fer.zemris.optjava.geneticAlgorithm.population.FixedSizePopulation;
import hr.fer.zemris.optjava.geneticAlgorithm.population.IPopulation;
import hr.fer.zemris.optjava.geneticAlgorithm.selection.ISelection;
import hr.fer.zemris.optjava.geneticAlgorithm.selection.RouletteWheel;
import hr.fer.zemris.optjava.geneticAlgorithm.selection.Tournament;
import hr.fer.zemris.optjava.simulatedAnnealing.Ifunction;
import hr.fer.zemris.optjava.simulatedAnnealing.solution.DoubleArraySolution;

public class SolveSystemFunction {
	private static final Random rand  = new Random();
	
	private static ISelection<DoubleArraySolution> makeSelection(String selection) {
		String[] parts = selection.split(":");
		switch(parts[0].toLowerCase()) {
		case "roulettewheel" : return new RouletteWheel<DoubleArraySolution>(rand);
		case "tournament" : return new Tournament<DoubleArraySolution>(rand, Integer.parseInt(parts[1]));
		}
		throw new RuntimeException("unsupported selection kind: " + selection);
	}
	
	private static int dim = 0;
	
	private static Ifunction makeProblem(String linearSystemDefinitionPath) throws IOException {
		Parser parser = new Parser(linearSystemDefinitionPath);
		IFunction linearSystem = parser.parseSystemFunction();
		dim = linearSystem.getVariableNum();
		
		Ifunction problem = new Ifunction() {
			@Override
			public double valueAt(double[] point) {
				Matrix p = new Matrix(point, dim);
				return linearSystem.f(p);
			}
		};
		
		/*Ifunction problem = new Ifunction() {
			@Override
			public double valeAt(double[] point) {
				return Math.abs(point[0]) + Math.abs(point[1]);
			}
		};
		dim = 2;*/
		
		return problem;
	}
	
	private static final double alpha = 0.9;

	private static double[] maxs = new double[] {10, 0, 3, 2, 1, 4};
	private static double[] mins = new double[] {7, -5, 0, 0, 0, 2};
	private static double[] deltas = new double[] {1e-2, 1e-3, 1e-4, 1e-4, 1e-4, 1e-3};
	
	public static void main(String[] args) throws IOException {
		String sizeOfPopulation = args[0];
		String desiredMinError = args[1];
		String maxIter = args[2];
		String selectionSelection = args[3];
		String sigma = args[4];
		String linearSystemDefinitionPath = args[5];
		
		Ifunction problem = makeProblem(linearSystemDefinitionPath); 
		IEvaluator<DoubleArraySolution> evaluator = new DoubleArraySolutionEvaluator(problem, true);
		
		int size = Integer.parseInt(sizeOfPopulation);
		IPopulation<DoubleArraySolution> population = new FixedSizePopulation<DoubleArraySolution>(size);
		for(int i = 0; i < size; ++i) {
			DoubleArraySolution chromosome = new DoubleArraySolution(dim);
			for(int j = 0; j < dim; ++j) {
				double l = maxs[j] - mins[j];
				chromosome.values[j] = rand.nextDouble() * l + mins[j];
			}
			evaluator.evaluate(chromosome);
			population.add(chromosome);
		}
		
		double minError = Double.parseDouble(desiredMinError);
		int maxIterations = Integer.parseInt(maxIter);
		ISelection<DoubleArraySolution> selection = makeSelection(selectionSelection);
		IMutation<DoubleArraySolution> mutation = new DoubleArraySolutionMutation(rand, Double.parseDouble(sigma));
		
		
		
		ICrossover<DoubleArraySolution> crossover = new DoubleArraySolutionCrossover(rand, alpha);
		
		GeneticAlgorithm<DoubleArraySolution> ga = new GeneticAlgorithm<DoubleArraySolution>();
		
		DoubleArraySolution solution = ga.run(rand, population, evaluator, selection, crossover, mutation, maxIterations, minError, false);
		System.out.println(solution.toString());
	}

}
