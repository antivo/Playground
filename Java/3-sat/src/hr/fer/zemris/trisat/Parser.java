package hr.fer.zemris.trisat;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Parser {
	private int numberOfVariables;
	private Clause[] clauses;
	
	private void parsePLine(String xs) {
		String[] words = xs.split("\\s+");
		int integer;
		for(int i = 0; i < words.length; ++i) {
			try {
				integer = Integer.parseInt(words[i]);
			} catch (Exception e) {
				continue;
			}
			if(integer < 1) {
				throw new IllegalArgumentException("Values in a p line can not be lesser than 1");
			}
			if(0 == this.numberOfVariables) {
				this.numberOfVariables = integer;
			} else if(null == clauses) {
				clauses = new Clause[integer];
				break;
			}
		}
	}
	
	private void parseDataLine(String xs, int clauseIndex) {
		if(null == clauses) {
			throw new  IllegalArgumentException("FIle is not in an expected form");
		}
		String[] words = xs.split("\\s+");
		int indexes[] = new int[words.length-1];
		int value;
		for(int i = 0; i < words.length; ++i) {
			value = Integer.parseInt(words[i]);
			if(0 == value) {
				break;
			} else {
				indexes[i] = value; 
			}
		}
		clauses[clauseIndex] = new Clause(indexes);
	}
	
	Parser(String filename) throws FileNotFoundException, IOException, IllegalArgumentException {
		this.numberOfVariables = 0;
		int clauseIndex = 0;
		BufferedReader br = new BufferedReader(new FileReader(filename));
		try {
	        String line = br.readLine();
	        while(line != null) {
	        	line = line.trim();
	        	if(line.charAt(0) == 'c') {
	        		// COMMENTS
	        	} else if (line.charAt(0) == '%') {
	        		break;
	        	} else if (line.charAt(0) == 'p') {
	        		parsePLine(line);
	        	} else {
	        		parseDataLine(line, clauseIndex);
	        		++clauseIndex;
	        	}
	            line = br.readLine();
	        }
	    } finally {
	        br.close();
	    }
		
		if(clauseIndex != clauses.length) {
			throw new IllegalArgumentException("Some data rows are missing from the file");
		}
	}
	
	public SATFormula getSATFormula() {
		return new SATFormula(numberOfVariables, clauses);
	}
}
