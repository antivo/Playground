package hr.fer.zemris.trisat;

public abstract class Algorithm {
	protected SATFormula satFormula;
	
	public Algorithm(SATFormula satFormula)  throws IllegalStateException  {
		this.satFormula = satFormula;
	}
	
	public abstract BitVector run();
}
