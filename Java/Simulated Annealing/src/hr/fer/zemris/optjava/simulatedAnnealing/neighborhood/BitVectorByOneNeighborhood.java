package hr.fer.zemris.optjava.simulatedAnnealing.neighborhood;

import hr.fer.zemris.optjava.simulatedAnnealing.solution.BitVectorSolution;

import java.util.Random;

public class BitVectorByOneNeighborhood implements INeighborhood<BitVectorSolution> {
	private final Random rand;
	
	public BitVectorByOneNeighborhood(Random rand) {
		this.rand = rand;
	}

	@Override
	public BitVectorSolution randomNeighbor(BitVectorSolution current) {
		BitVectorSolution neighbor = current.duplicate();
		int toChange = rand.nextInt(neighbor.bits.length);
		neighbor.bits[toChange] = !neighbor.bits[toChange];
		
		return neighbor;
	}
}
