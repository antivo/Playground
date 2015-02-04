package hr.fer.zemris.optjava.threesat.algorithm;

import hr.fer.zemris.optjava.threesat.BitVector;
import hr.fer.zemris.optjava.threesat.SATFormula;

public interface Algorithm {
	BitVector compute(SATFormula satFormula);
}
