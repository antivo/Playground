package hr.fer.zemris.ml.grouping.dataset;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Jama.Matrix;

public class Parser {
	private final static Charset ENCODING = StandardCharsets.UTF_8;
	private final Path fFilePath;
	private final Scanner scanner;
	private final String delimiter;
	private Scanner scannerInLine;
	
	public Parser(String aFileName, String delimiter) throws IOException {
		fFilePath = Paths.get(aFileName);
		scanner =  new Scanner(fFilePath, ENCODING.name());
		this.delimiter = delimiter;
	}
	
	public Dataset parse() {
		List<Matrix> result = new ArrayList<Matrix>();
		List<String> r = new ArrayList<String>();
		List<String> line= getNextLineEntry();
		while(null != line) {
			double[] array = new double[line.size() - 1];
			int idx = 0;
			int last = line.size() - 1;
			for(String s : line) {
				if(idx != last) {
					array[idx] = Double.parseDouble(s);
				} else {
					r.add(s);
				}
				++idx;
			}
			result.add(new Matrix(array, last));
			
			line = getNextLineEntry();
		}
		
		
		return new Dataset(result, r);
	}
	
	public List<String> getNextLineEntry() {
		List<String> result = null;
		if(scanner.hasNextLine()) {
			String line = scanner.nextLine();
			result = processLine(line);
		}
		return result;
	}

	private List<String> processLine(String aLine){
		List<String> result = null;
		scannerInLine = new Scanner(aLine);
	    scannerInLine.useDelimiter(delimiter);
	    if (scannerInLine.hasNext()){
	    	result = new ArrayList<String>();
	    	do {
	    		String newEntry = scannerInLine.next();
	    		result.add(newEntry);
	    	} while(scannerInLine.hasNext());
	    }
	    return result;
	}
}
