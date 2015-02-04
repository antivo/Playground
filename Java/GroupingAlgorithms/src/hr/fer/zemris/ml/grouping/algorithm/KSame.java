package hr.fer.zemris.ml.grouping.algorithm;

import hr.fer.zemris.ml.grouping.dataset.Dataset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import Jama.Matrix;

public class KSame {
	static private final double e = 0.000001;
	static private final int[] K2 = new int[] {4, 15};
	static private final int[] K3 = new int[] {0, 4, 15};
	static private final int[] K4 = new int[] {0, 2, 4, 15};
	static private final int[] K5 = new int[] {0, 2, 4, 9, 15};
	
	
	private static int[] selectK(int numOfGroups) {
		switch(numOfGroups) {
		case 2: return K2;
		case 3: return K3;
		case 4: return K4;
		case 5: return K5;
		default: throw new IllegalArgumentException("Unexpected number of groups. KSame::initializeCentroids");
		}
	}
	
	private static List<Matrix> initializeCentroids(Dataset dataset, int numOfGroups) {
		int[] k = selectK(numOfGroups);
		List<Matrix> centroids = new ArrayList<Matrix>();
		for(int idx : k) {
			Matrix centroid = dataset.get(idx);
			centroids.add(centroid);
		}
		
		return centroids;
	}
	
	private static double calculateEuclidianNorm(Matrix x, Matrix u) {
		Matrix x_u = x.minus(u);
		Matrix res = (x_u.transpose()).times(x_u);
		return res.get(0, 0);
	}
	
	private static double minFromArray(double[] arr) {
		double min = arr[0];
		for(int i = 1; i < arr.length; ++i) {
			if(min > arr[i]) {
				min = arr[i];
			}
		}
		
		return min;
	}
	
	private static double calculateJ(Dataset dataset, List<Matrix> centroids) {
		double J = 0;
		
		for(Matrix x : dataset) {
			double[] euclidian = new double[centroids.size()];
			int k = 0;
			for(Matrix u : centroids) {
				euclidian[k] = calculateEuclidianNorm(x, u);
				++k;
			}
			double b = minFromArray(euclidian);
			
			J += b;
		}
		
		return J;
	}
	
	private static List<Matrix> fixCentroids(Dataset dataset, List<Matrix> centroids) {
		int b[] = new int[dataset.getSize()];
		int idx = 0;
		for(Matrix x : dataset) {
			b[idx] = belongsToGroup(x, centroids);
			++idx;
		}
		// b oznacuje vektor kojem centroidu pripada
		
		
		List<Matrix> u = new ArrayList<Matrix>(); // nova lista centroida
		for(int i = 0; i < centroids.size(); ++i) {
			Matrix ui = null; // trenutni centroid
			int sumB = 0; // ukupno vektora koji mu pripadaju
			
			for(int j = 0; j < b.length; ++j) { // za svaku oznaku
				
				if(b[j] == i) { // AKO PRIPADA GRUPI
					++sumB;
					Matrix x = dataset.get(j); // VEKTOR KOJI PRIPADA GRUPI
					if(null == ui) {
						ui = x;
					} else {
						ui = ui.plus(x);
					}
				}
			}
			double t = 1./(double)sumB;
			
			ui = ui.times(t);
			u.add(ui);
		}
		return u;
	}
	
	public static List<Matrix> group(Dataset dataset, int groupNum) {
		List<Matrix> centroids = initializeCentroids(dataset, groupNum);
	
		double previousJ = 0; 
		double currentJ = calculateJ(dataset, centroids);
		do {
			previousJ = currentJ;	
			centroids = fixCentroids(dataset, centroids);
			currentJ = calculateJ(dataset, centroids);
		} while(Math.abs(currentJ - previousJ) > e);
		
		return centroids;
	}
	
	public static List<Double> getJ(Dataset dataset, int groupNum) {
		List<Double> results = new ArrayList<Double>();
		List<Matrix> centroids = initializeCentroids(dataset, groupNum);
		
		double previousJ = 0; 
		double currentJ = calculateJ(dataset, centroids);
		
		do {
			previousJ = currentJ;	
			results.add(previousJ);
			centroids = fixCentroids(dataset, centroids);
			currentJ = calculateJ(dataset, centroids);
		} while(Math.abs(currentJ - previousJ) > e);
	
		return results;
	}
	
	private static int belongsToGroup(Matrix x, List<Matrix> centroids) {
		double[] euclidian = new double[centroids.size()];
		int k = 0;
		for(Matrix u : centroids) {
			euclidian[k] = calculateEuclidianNorm(x, u);
			++k;
		}
		double min = minFromArray(euclidian);
		
		for(int i = 0; i < euclidian.length; ++i) {
			if(min == euclidian[i]) {
				return i;
			}
		}
		
		return 0;
	}
	
	public static int inGroup(Dataset dataset, List<Matrix> centroids, int id) {
		int sum = 0;
		for(Matrix x : dataset) {
			if(belongsToGroup(x, centroids) == id) {
				++sum;
			}
		}
		
		return sum;
	}
	
	public static String clarifyGroup(Dataset dataset, List<Matrix> centroids, int id) {
		List<Integer> indices = new ArrayList<Integer>();
		
		int idx = 0;
		for(Matrix x : dataset) {
			if(belongsToGroup(x, centroids) == id) {
				indices.add(idx);
			}
			++idx;
		}
		
		Set<String> allLabels = dataset.getLabelSet();
		Map<String, Integer> map = new HashMap<String, Integer>();
		
		for(int index : indices) {
			String label = dataset.getLabel(index);
			if(!map.containsKey(label)) {
				map.put(label, 1);
			} else {
				int r = map.get(label);
				map.put(label, r + 1);
			}
		}
		
		
		
		String out = "";
		boolean prva = true;
		
		for(String l : allLabels) {
			if(!map.containsKey(l)) {
				map.put(l, 0);
			}
		}
		
		while(map.size() != 0) {
			int max = 0;
			String key = "";
			for(Entry<String, Integer> e : map.entrySet()) {
				int val = e.getValue();
				if(val >= max) {
					max = val;
					key = e.getKey();
				}
			}
			
			if(prva) {
				prva = false;
			} else {
				out += ", ";
			}
			out += key + " " + max;
			map.remove(key);
		}
		
		
		return out;
	}
}
