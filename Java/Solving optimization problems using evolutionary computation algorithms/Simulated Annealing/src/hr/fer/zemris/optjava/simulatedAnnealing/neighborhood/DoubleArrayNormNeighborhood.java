package hr.fer.zemris.optjava.simulatedAnnealing.neighborhood;
import java.util.Random;

public class DoubleArrayNormNeighborhood extends AbstractArrayNeighborhood {
	private final Random rand;

	public DoubleArrayNormNeighborhood(Random rand, double[] deltas) {
		super(deltas);
		this.rand = rand;
	}
	
	@Override
	protected double getFromUnitInterval() {
		return rand.nextDouble();
	}
	
}
