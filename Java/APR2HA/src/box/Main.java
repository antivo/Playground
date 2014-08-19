package box;

import f.F2;
import f.F3;
import f.IFunction;
import hookeJeeves.HookeJeeves;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
	static private final Charset ENCODING = StandardCharsets.UTF_8;
	static private final String delimiter = " ";
	
	static private final double defaultALPHA= 1.3;
	static private final double defaultE = 10e-9;
	
	private static double[] expand(double d, int n) {
		double[] ret = new double[n];
		
		for(int i = 0; i < n; ++i) {
			ret[i] = d;
		}
		
		return ret;
	}
	
	private static double stringToDouble(String s) {
		return Double.parseDouble(s);
	}
	
	private static double[] getCoordFromStrings(List<String> l) {
		int n = l.size();
		double[] ret = new double[n];
		
		for(int i = 0; i < n; ++i) {
			String s = l.get(i);
			ret[i] = stringToDouble(s);
		}
		
		return ret;
	}
	
	public static void main(String[] args) {
		// FUN 2
		Path fFilePath = Paths.get(args[0]);
		List<String> startingPoint = new ArrayList<String>();
		String ALPHA = null;
		String E = null;
		Scanner scanner = null;
		try {
			scanner = new Scanner(fFilePath, ENCODING.name());
			if(scanner.hasNextLine()) {
				String line = scanner.nextLine();
				Scanner scannerInLine = new Scanner(line);
				scannerInLine.useDelimiter(delimiter);
				do {
		    		String newEntry = scannerInLine.next();
		    		startingPoint.add(newEntry);
		    	} while(scannerInLine.hasNext());
				scannerInLine.close();
			}
			
			if(scanner.hasNextLine()) 
				ALPHA = scanner.nextLine();
			
			
			if(scanner.hasNextLine()) 
				E = scanner.nextLine();
			
			scanner.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		IFunction fun2 = new F2();
		System.out.println("Function two:");
		
		double alpha2 = defaultALPHA;
		if(ALPHA != null) {
			alpha2 = stringToDouble(ALPHA);
		}
	
		double e2 = defaultE;
		if(E != null) {
			e2 = stringToDouble(E);
		}

		try {
		double[] r2 = Box.box(fun2, getCoordFromStrings(startingPoint), alpha2, e2);

		for(int i = 0; i < r2.length; ++i) {
			System.out.print(r2[i] + " ");
		}
		System.out.println();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		
		// FUN 3
		fFilePath = Paths.get(args[1]);
		startingPoint = new ArrayList<String>();
		List<String> parameters = new ArrayList<String>();
		ALPHA = null;
		E = null;
		try {
			scanner = new Scanner(fFilePath, ENCODING.name());
			if(scanner.hasNextLine()) {
				String line = scanner.nextLine();
				Scanner scannerInLine = new Scanner(line);
				scannerInLine.useDelimiter(delimiter);
				do {
		    		String newEntry = scannerInLine.next();
		    		startingPoint.add(newEntry);
		    	} while(scannerInLine.hasNext());
				scannerInLine.close();
			}
			
			if(scanner.hasNextLine()) {
				String line = scanner.nextLine();
				Scanner scannerInLine = new Scanner(line);
				scannerInLine.useDelimiter(delimiter);
				do {
		    		String newEntry = scannerInLine.next();
		    		parameters.add(newEntry);
		    	} while(scannerInLine.hasNext());
				scannerInLine.close();
			}
			
			if(scanner.hasNextLine()) 
				ALPHA = scanner.nextLine();
			
			
			if(scanner.hasNextLine()) 
				E = scanner.nextLine();
			
			scanner.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		IFunction fun3 = null;
		try {
			fun3 = new F3(getCoordFromStrings(parameters));
		} catch (Exception e4) {
			System.out.println(e4.getMessage());
			e4.printStackTrace();
		}
		System.out.println("Function three:");
		
		double alpha3 = defaultALPHA;
		if(ALPHA != null) {
			alpha3 = stringToDouble(ALPHA);
		}
	
		double e3 = defaultE;
		if(E != null) {
			e3 = stringToDouble(E);
		}

		try {
		double[] r3 = Box.box(fun3, getCoordFromStrings(startingPoint), alpha3, e3);
		
		for(int i = 0; i < r3.length; ++i) {
			System.out.print(r3[i] + " ");
		}
		System.out.println();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		

	}

}
