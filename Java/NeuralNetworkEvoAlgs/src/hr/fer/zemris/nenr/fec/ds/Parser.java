package hr.fer.zemris.nenr.fec.ds;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Parser {
	private final static Charset ENCODING = StandardCharsets.UTF_8;
	
	public static Dataset parse2D3Class(String filename) throws IOException {
		List<double[]> in  = new ArrayList<double[]>();
		List<double[]> out = new ArrayList<double[]>();
		
		Path fFilePath = Paths.get(filename);
		Scanner scanner =  new Scanner(fFilePath, ENCODING.name());
		while(scanner.hasNextLine()) {
			String line = scanner.nextLine();
			Scanner scannerInLine = new Scanner(line);

			double[] _2D = new double[2];
	    	for(int i = 0; i < 2; ++i) {
	    		String newEntry = scannerInLine.next();
	    		_2D[i] = Double.parseDouble(newEntry);
	    	}
	    	in.add(_2D);
	    	
	    	double[] _3Class = new double[3];
	    	for(int i = 0; i < 3; ++i) {
	    		String newEntry = scannerInLine.next();
	    		_3Class[i] = Double.parseDouble(newEntry);
	    	}
	    	out.add(_3Class);
		  
			scannerInLine.close();
		}
		scanner.close();
		
		return new Dataset(in, out);
	}

}

