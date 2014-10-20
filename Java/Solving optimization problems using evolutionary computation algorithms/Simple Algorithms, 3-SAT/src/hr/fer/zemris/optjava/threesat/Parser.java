package hr.fer.zemris.optjava.threesat;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

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
	
	private int[] processDeclarationLine(String line) {
		final int nums = 2;
		
		scannerInLine = new Scanner(line);
	    scannerInLine.useDelimiter(delimiter);
	    
	    int[] result = new int[nums];
	    if (scannerInLine.hasNext()){
	    	int offset = 2;
	    	int idx = 0;
	    	do {
	    		String newEntry = scannerInLine.next();
	    		if(offset > 0) {
	    			--offset;
	    		} else {
	    			result[idx++] = Integer.parseInt(newEntry);
	    		}
	    	} while(scannerInLine.hasNext() && idx < nums);
	    }
	    scannerInLine.close();
	    return result;
	}
	
	public SATFormula parse() {
		int numberOfVariables = 0;
		Clause[] clauses = null;
		
		int idx = 0;
		boolean run = true;
		while(run && scanner.hasNextLine()) {
			String line = scanner.nextLine();
			switch (line.charAt(0)) {
			case 'c': continue;
			case 'p': {
				int[] nums = processDeclarationLine(line);
				numberOfVariables = nums[0];
				clauses = new Clause[nums[1]];
				break;
			}
			case '%': {
				run = false;
				break;
			}
			default: {
				Clause clause = lineToClause(line);
				clauses[idx++] = clause;
				break;
			}
			}
		}
		scanner.close();
		SATFormula satFormula = new SATFormula(numberOfVariables, clauses);
		return satFormula;
	}
	

	private Clause lineToClause(String line){
		final int literals = 3;
		scannerInLine = new Scanner(line);
	    scannerInLine.useDelimiter(delimiter);
	    int[] result = new int[literals];
	    if (scannerInLine.hasNext()){
	    	int idx = 0;
	    	do {
	    		String newEntry = scannerInLine.next();
	    		result[idx++] = Integer.parseInt(newEntry);
	    	} while(scannerInLine.hasNext() && idx < literals);
	    }
	    scannerInLine.close();
	    return new Clause(result);
	}
}