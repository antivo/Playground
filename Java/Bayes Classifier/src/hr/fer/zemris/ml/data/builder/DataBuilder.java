package hr.fer.zemris.ml.data.builder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import Jama.Matrix;
import hr.fer.zemris.ml.data.Data;
import hr.fer.zemris.ml.parser.Parser;

public class DataBuilder {
	
	static public Data fromFile(String filename) throws IOException {
		Parser parser = new Parser(filename, " ");
		List<String> firstLine = parser.getNextLineEntry();
		return composeData(firstLine, parser);
	}
	
	static private Data composeData(List<String> firstLine, Parser parser) {
		int numOfLines = Integer.parseInt(firstLine.get(0));
		//int numOfClazz = Integer.parseInt(firstLine.get(1));
		List<Matrix> orderedExamples = new ArrayList<Matrix>();
		Map<String, List<Matrix>> data = collectData(numOfLines, parser, orderedExamples);
		
		return new Data(data, numOfLines, orderedExamples);
	}
	
	static private Map<String, List<Matrix>> collectData(int numOfLines, Parser parser, List<Matrix> lm) {
		Map<String, List<Matrix>> result = new HashMap<String, List<Matrix>>();
		for(int i = 0; i < numOfLines; ++i) {
			List<String> line = parser.getNextLineEntry();
			updateWithEntry(result, line, lm);
		}
		return result;
	}
	
	static private void updateWithEntry(Map<String, List<Matrix>> map, List<String> entries, List<Matrix> lm) {
		int listLength = entries.size();
		int entryLength = listLength - 1;
		double[] exampleData = new double[entryLength];
		Iterator<String> entriesIterator = entries.iterator();
		for(int i = 0; i < entryLength; ++i) {
			String elem = entriesIterator.next();
			exampleData[i] = Double.parseDouble(elem);
		}
		String clazz =  entriesIterator.next();
		
		Matrix matrix = new Matrix(exampleData, exampleData.length);
		if(map.containsKey(clazz)) {
			map.get(clazz).add(matrix);
		} else {
			List<Matrix> newList = new ArrayList<Matrix>();
			newList.add(matrix);
			map.put(clazz, newList);
		}
		lm.add(matrix);
	}
	
}
