package hr.fer.zemris.ml.parser;

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
	private final Path fFilePath;
	private final Scanner scanner;
	private final String delimiter;
	private Scanner scannerInLine;
	
	public Parser(String aFileName, String delimiter) throws IOException {
		fFilePath = Paths.get(aFileName);
		scanner =  new Scanner(fFilePath, ENCODING.name());
		this.delimiter = delimiter;
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
