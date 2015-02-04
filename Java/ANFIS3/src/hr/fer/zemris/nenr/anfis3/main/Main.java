package hr.fer.zemris.nenr.anfis3.main;

import java.util.List;
import java.util.Random;

import hr.fer.zemris.nenr.anfis3.dataset.Dataset;
import hr.fer.zemris.nenr.anfis3.fs.Rule;
import hr.fer.zemris.nenr.anfis3.nfs.NeruoFuzzySystem;
import hr.fer.zemris.nenr.anfis3.train.GradientDescent;

public class Main {

	public static void main(String[] args) {

		Random rand = new Random();
		Dataset dataset = new Dataset();
		NeruoFuzzySystem nfs = new NeruoFuzzySystem();
		GradientDescent gd = new GradientDescent(10e-7, 10e-50, 100);
		
		
		List<Rule> rules = gd.stohasticGradientDescent(2, nfs, rand, dataset);
		
		for(int i = -4; i <= 4; ++i) {
			for(int j = -4; j <= 4; ++j) {
				System.out.println(i + " " + " " + j + " " + dataset.get(i, j) + " " + nfs.output(i, j, rules));
			}
		}

	}

}
