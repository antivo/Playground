package hr.fer.zemris.optjava.no.main;

import Jama.Matrix;

public class Main {

	public static void main(String[] args) {
		double[][] r = new double[2][2];
		Matrix m = new Matrix(r);
		System.out.println(m.get(2, 2));

	}

}
