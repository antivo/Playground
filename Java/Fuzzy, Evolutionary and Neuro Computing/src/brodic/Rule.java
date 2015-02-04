package brodic;

import java.util.Map;
import java.util.Map.Entry;

import hr.fer.zemirs.fenc.set.FuzzySet;
import hr.fer.zemris.fenc.domain.DeclarationException;


public class Rule {
	private final Map<String, FuzzySet> antecedens;
	private final FuzzySet resultA;
	private final FuzzySet resultK;
	
	public Rule(Map<String, FuzzySet> map, FuzzySet resultA , FuzzySet resultK) {
		this.antecedens = map;
		this.resultA = resultA;
		this.resultK = resultK;
	}
	
	public FuzzySet applyMinK(Map<String, Double> map) throws DeclarationException {
		double accAntecedens = 1;
		
		for(Entry<String, FuzzySet> variable  : antecedens.entrySet()) {
			String key = variable.getKey();
			Double value = map.get(key);
			
			accAntecedens = Math.min(value, accAntecedens); //  operator 
		}
		
		
		return resultK.cut(accAntecedens);
	}
	
	public FuzzySet applyMinA(Map<String, Double> map) throws DeclarationException {
		double accAntecedens = 1;
		
		for(Entry<String, FuzzySet> variable  : antecedens.entrySet()) {
			String key = variable.getKey();
			Double value = map.get(key);
			
			accAntecedens = Math.min(value, accAntecedens); //  operator 
		}
		
		
		return resultA.cut(accAntecedens);
	}
	
	
	public FuzzySet applyProductA(Map<String, Double> map) throws DeclarationException {
		double accAntecedens = 1;
		
		for(Entry<String, FuzzySet> variable  : antecedens.entrySet()) {
			String key = variable.getKey();
			Double value = map.get(key);
			
			accAntecedens = value * accAntecedens; //  operator 
		}
		
		
		return resultA.scale(accAntecedens);
	}
	
	public FuzzySet applyProductK(Map<String, Double> map) throws DeclarationException {
		double accAntecedens = 1;
		
		for(Entry<String, FuzzySet> variable  : antecedens.entrySet()) {
			String key = variable.getKey();
			Double value = map.get(key);
			
			accAntecedens = value * accAntecedens; //  operator 
		}
		
		
		return resultA.scale(accAntecedens);
	}
}
