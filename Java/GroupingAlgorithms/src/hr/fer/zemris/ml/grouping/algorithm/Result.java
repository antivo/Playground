package hr.fer.zemris.ml.grouping.algorithm;

import java.util.List;

public class Result {
	final public List<GaussMixture> gml;
	final public List<Double> ls;
	
	public Result(List<GaussMixture> gml, List<Double> ls) {
		super();
		this.gml = gml;
		this.ls = ls;
	}
	
	
}
