package hr.fer.zemris.ml.grouping.main;

import hr.fer.zemris.ml.grouping.algorithm.EM;
import hr.fer.zemris.ml.grouping.algorithm.GaussMixture;
import hr.fer.zemris.ml.grouping.algorithm.KSame;
import hr.fer.zemris.ml.grouping.algorithm.Result;
import hr.fer.zemris.ml.grouping.dataset.Dataset;
import hr.fer.zemris.ml.grouping.dataset.KonfiguracijeParser;
import hr.fer.zemris.ml.grouping.dataset.Parser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import Jama.Matrix;

public class Main {
	
	private static void emAll(String outputDir, Dataset dataset, List<List<GaussMixture>> gmss, List<List<Double>> ls) throws FileNotFoundException, UnsupportedEncodingException {
		int[] order = new int[]{2, 3, 4, 5};
		
		boolean first = true;
		PrintWriter writer = new PrintWriter(outputDir + "em-all.dat", "UTF-8");
		for(int listIdx = 0; listIdx < order.length; ++listIdx) {
			if(first) {
				first = false;
			} else {
				writer.println("--");
			}
			
			String K = Integer.toString(order[listIdx]);
			String line= "K = " + K;
			writer.println(line);
			
			List<GaussMixture> gms = gmss.get(listIdx);
			
			for(int i = 1; i <= order[listIdx]; ++i) {
				line = "c" + i + ":"; 
				Matrix centroid = gms.get(i - 1).u;
				double[] c = centroid.getColumnPackedCopy();
				for(double val : c) {
					line += " " + String.format("%.2f", val);
				}
				writer.println(line);
				
				line = "grupa " + i + ": " + EM.inGroup(dataset, gms, i - 1) + " primjera";
				writer.println(line);
			}
			List<Double> l = ls.get(listIdx);
			line = "#iter: " + l.size();
			writer.println(line);
			
			line = "log-izglednost: " + l.get(l.size() - 1);
			writer.println(line);
		}

		writer.close();
		
	}
	
	private static void emK4(String outputDir, Dataset dataset, List<GaussMixture> gm) throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter(outputDir + "em-k4.dat", "UTF-8");
		
		List<List<String>> res = EM.allInGroups(dataset, gm);
		
		boolean first = true;
		for(int i = 1; i <= 4; ++i) {
			if(first) {
				first = false;
			} else {
				writer.println("--");
			}
			
			writer.println("Grupa " + i + ":");
			
			List<String> ls = res.get(i - 1);
			for(String s : ls) {
				writer.println(s);
			}
		}
		
		writer.close();
	}
	
	
	
	private static void kMeansAll(String outputDir, Dataset dataset, List<List<Matrix>> cs, List<List<Double>> js) throws FileNotFoundException, UnsupportedEncodingException {
		int[] order = new int[]{2, 3, 4, 5};
		
		boolean first = true;
		PrintWriter writer = new PrintWriter(outputDir + "kmeans-all.dat", "UTF-8");
		for(int listIdx = 0; listIdx < order.length; ++listIdx) {
			if(first) {
				first = false;
			} else {
				writer.println("--");
			}
			
			String K = Integer.toString(order[listIdx]);
			String line= "K = " + K;
			writer.println(line);
			
			List<Matrix> centroids = cs.get(listIdx);
			for(int i = 1; i <= order[listIdx]; ++i) {
				line = "c" + i + ":"; 
				Matrix centroid = centroids.get(i - 1);
				double[] c = centroid.getColumnPackedCopy();
				for(double val : c) {
					line += " " + String.format("%.2f", val);
				}
				writer.println(line);
				
				line = "grupa " + i + ": " + KSame.inGroup(dataset, centroids, i - 1) + " primjera";
				writer.println(line);
			}
			List<Double> j = js.get(listIdx);
			line = "#iter: " + j.size();
			writer.println(line);
			
			line = "J: " + j.get(j.size() - 1);
			writer.println(line);
		}

		writer.close();
	}
	
	private static void kMeansK4(String outputDir, Dataset dataset, List<Double> js, List<Matrix> centroidsK4) throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter(outputDir + "kmeans-k4.dat", "UTF-8");
		writer.println("#iteracije: J");
		writer.println("--");
		int counter = 0;
		for(double j : js) {
			writer.println("#" + counter + ": " + j);
			++counter;
		}
		writer.println("--");
		for(int i = 1; i <=centroidsK4.size(); ++i) {
			String line = "Grupa " + i + ": "  + KSame.clarifyGroup(dataset, centroidsK4, i - 1);
			writer.println(line);
		}
		
		writer.close();
	}
	
	private static void emKonf(String outputDir, List<Result> results) throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter(outputDir + "em-konf.dat", "UTF-8");
		
		int count = 1;
		boolean first = true;
		for(Result r : results) {
			if(first) {
				first = false;
			} else {
				writer.println("--");
			}
			
			writer.println("Konfiguracija " + count + ":");
			writer.println("log-izglednost:" + ": "  + r.ls.get(r.ls.size() - 1));
			writer.println("#iteracija:" + ": "  + r.ls.size());
			++count;
		}
		
		writer.close();
	}
	
	private static void emKmeans(String outputDir, Dataset dataset, List<Double> ls, List<GaussMixture> gm) throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter(outputDir + "em-kmeans.dat", "UTF-8");
		writer.println("#iteracije:  log-izglednost");
		writer.println("--");
		int counter = 0;
		for(double l : ls) {
			writer.println("#" + counter + ": " + l);
			++counter;
		}
		writer.println("--");
		for(int i = 1; i <= gm.size(); ++i) {
			String line = "Grupa " + i + ": "  + EM.clarifyGroup(dataset, gm, i - 1);
			writer.println(line);
		}
		
		writer.close();
	}

	
	public static void main(String[] args) throws IOException {		
		String input = args[0];
		String conf = args[1];
		String outputDir = args[2];
		
		Parser parser = new Parser(input, " ");
		Dataset dataset = parser.parse();
		
		KonfiguracijeParser confParser = new KonfiguracijeParser(conf, " ");
		List<Dataset> datas = confParser.parse();
		
		List<Matrix> centroidsK2 = KSame.group(dataset, 2);
		List<Double> JK2         = KSame.getJ(dataset, 2);
		List<Matrix> centroidsK3 = KSame.group(dataset, 3);
		List<Double> JK3         = KSame.getJ(dataset, 3);
		List<Matrix> centroidsK4 = KSame.group(dataset, 4);
		List<Double> JK4         = KSame.getJ(dataset, 4);
		List<Matrix> centroidsK5 = KSame.group(dataset, 5);
		List<Double> JK5         = KSame.getJ(dataset, 5);
		
		
		List<List<Matrix>> list = new ArrayList<List<Matrix>>();
		list.add(centroidsK2);
		list.add(centroidsK3);
		list.add(centroidsK4);
		list.add(centroidsK5);
		List<List<Double>> js = new ArrayList<List<Double>>();
		js.add(JK2);
		js.add(JK3);
		js.add(JK4);
		js.add(JK5);
		kMeansAll(outputDir, dataset, list, js);
		kMeansK4(outputDir, dataset, JK4, centroidsK4);
		
		/////////////////////////////////////////
		
		Result r2 = EM.group(dataset, 2, null, null);
		Result r3 = EM.group(dataset, 3, null, null);
		Result r4 = EM.group(dataset, 4, null, null);
		Result r5 = EM.group(dataset, 5, null, null);
		
		List<GaussMixture> gmK2 = r2.gml;
		List<Double> LK2        = r2.ls;
		List<GaussMixture> gmK3 = r3.gml;
		List<Double> LK3        = r3.ls;
		List<GaussMixture> gmK4 = r4.gml;
		List<Double> LK4        = r4.ls;
		List<GaussMixture> gmK5 = r5.gml;
		List<Double> LK5        = r5.ls;
		
		List<List<GaussMixture>> GMlist = new ArrayList<List<GaussMixture>>();
		GMlist.add(gmK2);
		GMlist.add(gmK3);
		GMlist.add(gmK4);
		GMlist.add(gmK5);
		List<List<Double>> ls = new ArrayList<List<Double>>();
		ls.add(LK2);
		ls.add(LK3);
		ls.add(LK4);
		ls.add(LK5);
		
		emAll(outputDir, dataset, GMlist, ls);
		emK4(outputDir, dataset, gmK4);
		
		List<Result> results = new ArrayList<Result>();
		for(Dataset d : datas) {
			Result r = EM.group(dataset, 4, null, d);
			results.add(r);
		}
		emKonf(outputDir, results);
		
		Result rK4 = EM.group(dataset, 4, centroidsK4, null);
		emKmeans(outputDir, dataset, rK4.ls, rK4.gml);
	}

}
