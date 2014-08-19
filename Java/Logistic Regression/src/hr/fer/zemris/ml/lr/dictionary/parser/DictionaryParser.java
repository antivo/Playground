package hr.fer.zemris.ml.lr.dictionary.parser;

import hr.fer.zemris.ml.lr.dictionary.Dictionary;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DictionaryParser {
private final static Charset ENCODING = StandardCharsets.UTF_8;
	
	public static Dictionary parse(String filename) throws IOException {
		List<Integer> frequencies  = new ArrayList<Integer>();
		List<String> words = new ArrayList<String>();
		
		Path fFilePath = Paths.get(filename);
		Scanner scanner =  new Scanner(fFilePath, ENCODING.name());
		while(scanner.hasNextLine()) {
			String line = scanner.nextLine();
			Scanner scannerInLine = new Scanner(line);

			String word = scannerInLine.next();
	    	words.add(word);
	    	
	    	String frequency = scannerInLine.next();
	    	frequencies.add(Integer.parseInt(frequency));
		  
			scannerInLine.close();
		}
		scanner.close();
		
		return new Dictionary(words, frequencies);
	}
}
