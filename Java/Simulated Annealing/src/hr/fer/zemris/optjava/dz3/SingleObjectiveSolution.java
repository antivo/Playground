package hr.fer.zemris.optjava.dz3;

public class SingleObjectiveSolution implements IDecoder<SingleObjectiveSolution>, INeighborhood<SingleObjectiveSolution>, IOptAlgorithm<SingleObjectiveSolution>, Comparable<SingleObjectiveSolution>{
	private double fitness;
	private double value;
	
	public SingleObjectiveSolution() {
		
	}
	
	public int compareTo(SingleObjectiveSolution other) {
		if (this.fitness < other.fitness) {
			return -1;
		} else if(this.fitness > other.fitness) {
			return 1;
		}
		
		return 0;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void randomNeighbor(SingleObjectiveSolution neighnoor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double[] decode(SingleObjectiveSolution code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void decode(SingleObjectiveSolution code, double[] result) {
		// TODO Auto-generated method stub
		
	}
}
