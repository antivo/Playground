package hr.fer.zemris.optjava.simulatedAnnealing.neighborhood;
import java.util.Random;


public class DoubleArrayUnifNeighborhood extends AbstractArrayNeighborhood {
	private final Random rand;
	
	public DoubleArrayUnifNeighborhood(Random rand, double[] deltas) {
		super(deltas);
		this.rand = rand;
	}

	@Override
	protected double getFromUnitInterval() {
		return rand.nextGaussian();
	}
	
}
