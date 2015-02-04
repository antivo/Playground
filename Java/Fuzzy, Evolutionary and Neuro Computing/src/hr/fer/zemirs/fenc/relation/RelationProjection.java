package hr.fer.zemirs.fenc.relation;

import hr.fer.zemirs.fenc.set.FuzzySet;
import hr.fer.zemris.fenc.domain.Cartesian;
import hr.fer.zemris.fenc.domain.DeclarationException;
import hr.fer.zemris.fenc.domain.Domain;

public class RelationProjection {
	private static Domain extractExcept(FuzzySet fuzzySet, Domain target) {
		Domain d = fuzzySet.getDomain();
		
		if(d instanceof Cartesian) {
			Cartesian dd = (Cartesian) d;
			try {
				if(target instanceof Cartesian) {
					return dd.getExcept(((Cartesian) target).getDomains());
				} else {
					return dd.getExcept(target);
				}
			} catch (DeclarationException e) {
				return null;
			}
		}
		return null;
	}
	
	static private int[] getPositions(FuzzySet fs, Domain target) {
		Domain d = fs.getDomain();
		if(d instanceof Cartesian) {
			Domain[] targets;
			if(target instanceof Cartesian) {
				targets = ((Cartesian) target).getDomains();
			} else {
				targets = new Domain[]{target};
			}

			int[] result = new int[targets.length];
			Domain[] originalDomains = ((Cartesian) d).getDomains();
			
			
			int idx = 0;
			int[] positions = new int[targets.length];
			for(int i = 0; i < originalDomains.length; ++i) {
				if(originalDomains[i].equals(targets[idx])) {
					positions[idx] = i;
					++idx;
				}
			}
			
			if(idx != targets.length) {
				return null;
			}
			
			return positions;
		}
		return null;
	}
	
	static private double calculateMembership(int[] positions, String... args) {
		
		
		
		return 0;
	}
	
	static public FuzzySet projectRelation(FuzzySet fs, Domain d) {
		int[] positions = getPositions(fs,d);
		if(null != positions) {
			/*for(int i = 0; i < positions.length; ++i) {
				System.out.println(positions[i]);
			}*/
			
			
		}
		
		return null;
	}
}
