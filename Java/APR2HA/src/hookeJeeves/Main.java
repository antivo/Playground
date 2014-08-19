package hookeJeeves;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import f.F1;
import f.F2;
import f.F3;
import f.F4;
import f.IFunction;

public class Main {
	static private final Charset ENCODING = StandardCharsets.UTF_8;
	static private final String delimiter = " ";
	
	static private final double defaultDX = 0.5;
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
		Path fFilePath = Paths.get(args[0]);
		Scanner scanner;
		
		List<String> startingPoint = new ArrayList<String>();
		String DX = null;
		String E = null;
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
				DX = scanner.nextLine();
			
			
			if(scanner.hasNextLine()) 
				E = scanner.nextLine();
			
			scanner.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		IFunction fun1 = new F1();
		
		
		double dx1 = defaultDX;
		if(DX != null) {
			dx1 = stringToDouble(DX);
		}
	
		double e1 = defaultE;
		if(E != null) {
			e1 = stringToDouble(E);
		}

		int n1 = startingPoint.size();
		try {
		double[] r1 = HookeJeeves.hookeJeeves(fun1, getCoordFromStrings(startingPoint), expand(dx1, n1), expand(e1, n1));
		
		
		System.out.println("\nFunction one:");
		for(int i = 0; i < r1.length; ++i) {
			System.out.print(r1[i] + " ");
		}
		System.out.println();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		// FUN 2
		/*fFilePath = Paths.get(args[1]);
		startingPoint = new ArrayList<String>();
		DX = null;
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
			
			if(scanner.hasNextLine()) 
				DX = scanner.nextLine();
			
			
			if(scanner.hasNextLine()) 
				E = scanner.nextLine();
			
			scanner.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		IFunction fun2 = new F2();
		System.out.println("Function two:");
		
		double dx2 = defaultDX;
		if(DX != null) {
			dx2 = stringToDouble(DX);
		}
	
		double e2 = defaultE;
		if(E != null) {
			e2 = stringToDouble(E);
		}

		int n2 = startingPoint.size();
		try {
		double[] r2 = HookeJeeves.hookeJeeves(fun2, getCoordFromStrings(startingPoint), expand(dx2, n2), expand(e2, n2));
		
		for(int i = 0; i < r2.length; ++i) {
			System.out.print(r2[i] + " ");
		}
		System.out.println();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		
		// FUN 3
		fFilePath = Paths.get(args[2]);
		startingPoint = new ArrayList<String>();
		List<String> parameters = new ArrayList<String>();
		DX = null;
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
				DX = scanner.nextLine();
			
			
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
		
		double dx3 = defaultDX;
		if(DX != null) {
			dx3 = stringToDouble(DX);
		}
	
		double e3 = defaultE;
		if(E != null) {
			e3 = stringToDouble(E);
		}

		int n3 = startingPoint.size();
		try {
		double[] r3 = HookeJeeves.hookeJeeves(fun3, getCoordFromStrings(startingPoint), expand(dx3, n3), expand(e3, n3));
		
		for(int i = 0; i < r3.length; ++i) {
			System.out.print(r3[i] + " ");
		}
		System.out.println();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		
		// FUN 4
		fFilePath = Paths.get(args[3]);
		startingPoint = new ArrayList<String>();
		DX = null;
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
			
			if(scanner.hasNextLine()) 
				DX = scanner.nextLine();
			
			
			if(scanner.hasNextLine()) 
				E = scanner.nextLine();
			
			scanner.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		IFunction fun4 = new F4();
		System.out.println("Function four:");
		
		double dx4 = defaultDX;
		if(DX != null) {
			dx4 = stringToDouble(DX);
		}
	
		double e4 = defaultE;
		if(E != null) {
			e4 = stringToDouble(E);
		}

		int n4 = startingPoint.size();
		try {
		double[] r4 = HookeJeeves.hookeJeeves(fun4, getCoordFromStrings(startingPoint), expand(dx4, n4), expand(e4, n4));
		
		for(int i = 0; i < r4.length; ++i) {
			System.out.print(r4[i] + " ");
		}
		System.out.println();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		*/
		

	}

}
