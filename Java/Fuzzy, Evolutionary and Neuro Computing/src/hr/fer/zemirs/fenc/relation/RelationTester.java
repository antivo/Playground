package hr.fer.zemirs.fenc.relation;

import hr.fer.zemirs.fenc.set.FuzzySet;
import hr.fer.zemris.fenc.domain.Cartesian;
import hr.fer.zemris.fenc.domain.DeclarationException;
import hr.fer.zemris.fenc.domain.Domain;
import hr.fer.zemris.fenc.domain.DomainException;
import hr.fer.zemris.fenc.element.CartesianElement;

public class RelationTester {
	private static Cartesian extractIf2D(FuzzySet fuzzySet) {
		Domain d = fuzzySet.getDomain();
		
		if(d instanceof Cartesian) {
			Cartesian dd = (Cartesian) d;
			if(dd.isRelation()) {
				try {
					if(2 == dd.realationOver()) {
						return (Cartesian)d; 
					}
				} catch (Exception e) {}
			}
		}
		
		return null;
	}
	
	
	public static boolean testSymetric(FuzzySet fuzzySet) {
		Cartesian c = extractIf2D(fuzzySet);
		if(null != c) {
			String[] elems = c.getDomainComponents();
			for(String elem : elems) {
				try {
					String[] components = CartesianElement.getElemsFromProduct(elem);
					String a = components[0];
					String b = components[1];
					
					String ba = b + ',' + a;
					
					try {
						if(fuzzySet.getMembershipFor(elem) != fuzzySet.getMembershipFor(ba)) {
							return false;
						}
					} catch (DomainException e) {}
				} catch (DeclarationException e) {}
			}
		}
		return true;
	}
	
	public static boolean testReflexive(FuzzySet fuzzySet) {
		Cartesian c = extractIf2D(fuzzySet);
		if(null != c) {
			String[] elems = c.getDomainComponents();
			for(String elem : elems) {
				try {
					String[] components = CartesianElement.getElemsFromProduct(elem);
					String a = components[0];
					String b = components[1];
					
					String aa = a + ',' + a;
					
					try {
						if(fuzzySet.getMembershipFor(aa) != 1) {
							return false;
						}
					} catch (DomainException e) {}
				} catch (DeclarationException e) {}
			}
		}
		return true;
	}
	
	private static double max_min(FuzzySet fs, String x, String z, String[] ys) throws DeclarationException, DomainException {
		double max = 0;
		for(String y : ys) {
			String xy = x + "," + y;
			String yz = y + "," + z;
			
			double xyVal = fs.getMembershipFor(xy);
			double yzVal = fs.getMembershipFor(yz);
			
			double min = Math.min(xyVal, yzVal);
			if(min > max) {
				max = min;
			}
			
		}
		return max;
	}
	
	private static double max_product() {
		return 0;
	}
	
	public static boolean testTransitive(FuzzySet fuzzySet, String m) {
		Cartesian c = extractIf2D(fuzzySet);
		if(null != c) {
			Domain d = c.getSingular();
			String[] singularElems = d.getDomainComponents();
			
			for(String x : singularElems) {
				for(String y : singularElems) {
					for(String z : singularElems) {
						double valXZ = 0;
						double valXY = 0;
						double valYZ = 0;
						
						switch(m) {
						case "max-min" : {
							try {
								valXZ = max_min(fuzzySet, x, z, singularElems);
								valXY = max_min(fuzzySet, x, y, singularElems);
								valYZ = max_min(fuzzySet, y, z, singularElems);
							} catch (DeclarationException | DomainException e) {
								return false;
							}
							
							break;
						}
						case "max-product" : {
							
							break;
						}
						default : throw new IllegalArgumentException("Unsuported");
						}
						
						if(valXZ < Math.min(valXY, valYZ)) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}
	
	public static boolean testFuzzyEquivalence(FuzzySet fuzzySet, String m) {
		if(testReflexive(fuzzySet) && testSymetric(fuzzySet) && testTransitive(fuzzySet, m)) {
			return true;
		} 
		return false;
	}
	
}
