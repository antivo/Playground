package hr.fer.zemris.ml.grouping.algorithm;

import hr.fer.zemris.ml.grouping.dataset.Dataset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import Jama.Matrix;

public class EM {
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
	
	private static List<GaussMixture> initializeMixture(Dataset dataset, int groupNum) {
		List<GaussMixture> gm = new ArrayList<GaussMixture>();
		
		int d = dataset.get(0).getRowDimension();
		
		int[] k = selectK(groupNum);
		for(int idx : k) {
			double pi = 1./(double)groupNum;
			Matrix E = Matrix.identity(d, d);
			Matrix centroid = dataset.get(idx);
			gm.add(new GaussMixture(pi, centroid, E));
		}
		
		return gm;
	}
	
	private static List<GaussMixture> initializeMixture(Dataset dataset, int groupNum, List<Matrix> centroids) {
		List<GaussMixture> gm = new ArrayList<GaussMixture>();
		
		int d = dataset.get(0).getRowDimension();
		
		for(int i = 0; i < 4; ++i) {
			double pi = 1./(double)groupNum;
			Matrix E = Matrix.identity(d, d);
			Matrix centroid = centroids.get(i);
			gm.add(new GaussMixture(pi, centroid, E));
		}
		
		return gm;
	}
	
	private static List<GaussMixture> initializeMixture(Dataset dataset, int groupNum, Dataset centroids) {
		List<GaussMixture> gm = new ArrayList<GaussMixture>();
		
		int d = dataset.get(0).getRowDimension();
		
		for(Matrix m : centroids) {
			double pi = 1./(double)groupNum;
			Matrix E = Matrix.identity(d, d);
			Matrix centroid = m;
			gm.add(new GaussMixture(pi, centroid, E));
		}
		
		return gm;
	}
	
	
	public static double calculateLogProbability(Dataset dataset, List<GaussMixture> gm) {
		double rez = 0;
		for(int i = 0; i < dataset.getSize(); ++i) {
			double sum = 0;
			Matrix x = dataset.get(i);
			for(int k = 0; k < gm.size(); ++k) {
				sum += gm.get(k).responsibility(x);
			}
			rez += Math.log(sum);
		}
		return rez;
	}
	
	public static Result group(Dataset dataset, int groupNum, List<Matrix> centroids, Dataset followingDataset) {
		List<Double> result = new ArrayList<Double>();
		
		
		
		List<GaussMixture> gm = null;
		if (followingDataset != null) {
			gm = initializeMixture(dataset, groupNum, followingDataset);
		}
		if(centroids != null) {
			gm = initializeMixture(dataset, groupNum, centroids);
		} else {
			gm = initializeMixture(dataset, groupNum);
		}
		
		double previousLog = 0; 
		double currentLog = calculateLogProbability(dataset, gm);
		do {
			previousLog = currentLog;	
			// E korak
			double[][] h = new double[dataset.getSize()][groupNum];
			int idx = 0;
			for(Matrix m : dataset) {
				double n = 0;
				for(int k = 0; k < groupNum; ++k) {
					n += gm.get(k).responsibility(m);
				}
				
				for(int k = 0; k < groupNum; ++k) {
					h[idx][k] = gm.get(k).responsibility(m) / n;
				}
				++idx;
			}
			
			// M korak
			for(int k = 0; k < groupNum; ++k) {
				Matrix uk = null;
				for(int i = 0; i < dataset.getSize(); ++i) {
					Matrix component = dataset.get(i).times(h[i][k]);
					if(uk == null) {
						uk = component;
					} else {
						uk = uk.plus(component);
					}
				}
				
				double n = 0;
				for(int i = 0; i < dataset.getSize(); ++i) {
					n += h[i][k];
				}
				
				uk = uk.times(1./n);
				
				//
				
				Matrix Ek = null;
				for(int i = 0; i < dataset.getSize(); ++i) {
					Matrix component = dataset.get(i).minus(uk);
					component = component.times(component.transpose()).times(h[i][k]);
					if(Ek == null) {
						Ek = component;
					} else {
						Ek = Ek.plus(component);
					}
				}
				
				Ek = Ek.times(1./n);
				
				//
				
				double pik = n/(double)dataset.getSize();
				
				gm.set(k, new GaussMixture(pik, uk, Ek));	
			}
			result.add(currentLog);
			currentLog = calculateLogProbability(dataset, gm);
		} while(Math.abs(currentLog - previousLog) > 0.00001);
		if(Double.isInfinite(currentLog) || Double.isNaN(currentLog)) {
			result.add(Double.POSITIVE_INFINITY);
		}
		
		return new Result(gm, result);
	}
	
	private static int belongsToGroup(Matrix x, List<GaussMixture> gm) {
		double[] responsibility = new double[gm.size()];
		int k = 0;
		for(GaussMixture u : gm) {
			responsibility[k] = u.responsibility(x);
			++k;
		}
		double max = maxFromArray(responsibility);
		
		for(int i = 0; i < responsibility.length; ++i) {
			if(max == responsibility[i]) {
				return i;
			}
		}
		
		return 0;
	}
	
	private static double maxFromArray(double[] arr) {
		double max = arr[0];
		for(int i = 1; i < arr.length; ++i) {
			if(max < arr[i]) {
				max = arr[i];
			}
		}
		
		return max;
	}
	
	public static int inGroup(Dataset dataset, List<GaussMixture> gm, int id) {
		int sum = 0;
		for(Matrix x : dataset) {
			if(belongsToGroup(x, gm) == id) {
				++sum;
			}
		}
		
		return sum;
	}
	
	public static List<List<String>> allInGroups(Dataset dataset, List<GaussMixture> gm) {
		List<List<String>> lls = new ArrayList<List<String>>();
		List<List<Double>> lld = new ArrayList<List<Double>>();
		
		lls.add(new ArrayList<String>());
		lls.add(new ArrayList<String>());
		lls.add(new ArrayList<String>());
		lls.add(new ArrayList<String>());
		
		lld.add(new ArrayList<Double>());
		lld.add(new ArrayList<Double>());
		lld.add(new ArrayList<Double>());
		lld.add(new ArrayList<Double>());
		
		for(int i = 0; i < dataset.getSize(); ++i) {
			Matrix x = dataset.get(i);
			String label = dataset.getLabel(i);
			
			int group = belongsToGroup(x, gm);
			
			double responsibility = gm.get(group).h(x, gm);
			
			lls.get(group).add(label + " " + String.format("%.2f", responsibility));
			lld.get(group).add(responsibility);
		}	
		return lls;
	}
	
	public static String clarifyGroup(Dataset dataset, List<GaussMixture> gm, int id) {
		List<Integer> indices = new ArrayList<Integer>();
		
		int idx = 0;
		for(Matrix x : dataset) {
			if(belongsToGroup(x, gm) == id) {
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
