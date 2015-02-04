package hr.fer.zemris.optjava.geneticAlgorithm.operator;

import hr.fer.zemris.optjava.simulatedAnnealing.Ifunction;
import hr.fer.zemris.optjava.simulatedAnnealing.solution.DoubleArraySolution;

public class DoubleArraySolutionEvaluator implements IEvaluator<DoubleArraySolution> {
	private final Ifunction function;
	private final boolean  minimize;

	public DoubleArraySolutionEvaluator(Ifunction function, boolean minimize) {
		this.function = function;
		this.minimize = minimize;
	}

	@Override
	public void evaluate(DoubleArraySolution solution) {
		solution.value = function.valueAt(solution.values);
		solution.fitness = solution.value;
		if(minimize) {
			solution.fitness = -solution.fitness;
		}
	}

	
}
