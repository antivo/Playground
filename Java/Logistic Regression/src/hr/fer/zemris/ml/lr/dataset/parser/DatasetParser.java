package hr.fer.zemris.ml.lr.dataset.parser;

import hr.fer.zemris.ml.lr.dataset.Dataset;
import hr.fer.zemris.ml.lr.dataset.entry.Entry;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class DatasetParser {
	private final static Charset ENCODING = StandardCharsets.UTF_8;
	
	public static Dataset parse(String filename) throws IOException {
		List<Entry> entries = new ArrayList<Entry>();
		
		Path fFilePath = Paths.get(filename);
		Scanner scanner =  new Scanner(fFilePath, ENCODING.name());
		while(scanner.hasNextLine()) {
			String line = scanner.nextLine();
			Scanner scannerInLine = new Scanner(line);

			String label = scannerInLine.next();
			Map<Integer, Double> features = new HashMap<Integer, Double>();
			while(scannerInLine.hasNext()) {
				String inLine = scannerInLine.next();
				
				String[] pair = inLine.split(":");
				String wordIndex = pair[0];
				String scaledFrequency = pair[1];
				
				features.put(Integer.parseInt(wordIndex), Double.parseDouble(scaledFrequency));
	    	}
			Entry entry = new Entry(Integer.parseInt(label), features);
	    	entries.add(entry);
			
			scannerInLine.close();
		}
		scanner.close();
		
		return new Dataset(entries);
	}

}
