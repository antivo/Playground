package main;

import function.F3;
import function.F4;
import function.F6;
import function.F7;
import function.IFunction;
import algorithm.GeneticAlgorithm;

public class Main {

	public static void main(String[] args) throws Exception {
		String sizeOfPopulation = args[0];
		String pc = args[1];
		String iterations = args[2];

		GeneticAlgorithm ga = new GeneticAlgorithm(Integer.parseInt(sizeOfPopulation), Double.parseDouble(pc), Integer.parseInt(iterations));
	
		System.out.println("F3");
		IFunction f3 = new F3(90,6,17,-11,88);
		ga.findMin(f3);
	
		System.out.println();
		System.out.println("F4");
		IFunction f4 = new F4();
		ga.findMin(f4);
		
		System.out.println();
		System.out.println("F6");
		IFunction f6 = new F6(5);
		ga.findMin(f6);
		
		System.out.println();
		System.out.println("F7");
		IFunction f7 = new F7(5);
		ga.findMin(f7);
	}

}
