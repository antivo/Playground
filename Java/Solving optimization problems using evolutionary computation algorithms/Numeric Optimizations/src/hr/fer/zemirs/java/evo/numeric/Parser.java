package hr.fer.zemirs.java.evo.numeric;
import hr.fer.zemirs.java.evo.numeric.function.LinearSystem;
import hr.fer.zemirs.java.evo.numeric.function.SystemFunction;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import Jama.Matrix;


public class Parser {
	private final static Charset ENCODING = StandardCharsets.UTF_8;
	private final Path fFilePath;
	private final Scanner scanner;
	
	public Parser(String aFileName) throws IOException {
		fFilePath = Paths.get(aFileName);
		scanner =  new Scanner(fFilePath, ENCODING.name());
	}
	
	private static void assertLine(String line) {
		if(line.length() < 3 || line.charAt(0) != '[' || ']' != line.charAt(line.length() - 1)) {
			throw new RuntimeException("equasion must be inside [] bracers");
		}
	}
	
	private double[] getLine(String line) {
		assertLine(line);
		line = line.substring(1, line.length() - 1);
		String[] coefs = line.split(",");
		
		double[] res = new double[coefs.length];
		for(int i = 0; i < coefs.length; ++i) {
			String coef = coefs[i].trim();
			res[i] = Double.parseDouble(coef);
		}
		return res;
	}
	
	private System getSystem() {
		List<double[]> rows = new ArrayList<double[]>();
		while(scanner.hasNextLine()) {
			String line = scanner.nextLine();
			switch (line.charAt(0)) {
			case '#': continue;
			default: {
				double[] r = getLine(line);
				rows.add(r);
			}
			}
		}
		
		System system = null;
		if(!rows.isEmpty()) {
			int dim = rows.get(0).length - 1;
			//java.lang.System.out.println(dim);
			Matrix A = new Matrix(rows.size(), dim);
			Matrix b = new Matrix(rows.size(), 1);
			
			int idx = 0;
			for(double[] row : rows) {
				double[] r = Arrays.copyOf(row, dim);
				for(int j = 0; j < r.length; ++j) {
					
					A.set(idx, j, r[j]);
				}
				//java.lang.System.out.println(dim);
				double y = row[dim];
				b.set(idx, 0, y);
				
				++idx;
			}
			system = new System(A, b);
		}	
		return system;
	}
	
	public LinearSystem parseLinearSystem() {
		System system = getSystem();
		LinearSystem linearSystem = new LinearSystem(system.A, system.b);
		return linearSystem;
	}
	
	public SystemFunction parseSystemFunction() {
		System system = getSystem();
		SystemFunction systemFunction = new SystemFunction(system.A, system.b);
		return systemFunction;
	}
	
	class System {
		public final Matrix A;
		public final Matrix b;
		
		public System(Matrix a, Matrix b) {
			this.A = a;
			this.b = b;
		}

		public Matrix getA() {
			return A;
		}

		public Matrix getB() {
			return b;
		}
	}
}
