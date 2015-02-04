package hr.fer.zemris.optjava.simulatedAnnealing.solution;


public abstract class SingleObjectiveSolution implements Comparable<SingleObjectiveSolution> {
	public double fitness;
	public double value;
	
	public SingleObjectiveSolution() {}

	@Override
	public int compareTo(SingleObjectiveSolution o) {
		return Double.compare(this.fitness, o.fitness);
	}
	
	public abstract SingleObjectiveSolution duplicate();
	
	public abstract SingleObjectiveSolution newLikeThis();
	
	protected static String format(double value) {
		return String.format("%.5f", value);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("VALUE: ").append(format(value)).append(" ").append("FIT: ").append(format(fitness));
		return sb.toString();
	}
}
