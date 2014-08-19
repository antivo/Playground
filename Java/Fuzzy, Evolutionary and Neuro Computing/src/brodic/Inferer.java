package brodic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hr.fer.zemirs.fenc.set.FuzzySet;
import hr.fer.zemris.fenc.domain.DeclarationException;
import hr.fer.zemris.fenc.domain.Domain;
import hr.fer.zemris.fenc.domain.DomainException;


// zakljucivatelj
public class Inferer {

	public static double CoA(FuzzySet A) throws DeclarationException, DomainException {
		Domain d = A.getDomain();
		
		double b = 0;
		double n = 0;
		for(String x : d.getDomainComponents()) {
			double u = A.getMembershipFor(x); 
			b += u * Double.parseDouble(x);
			n += u;
		}

		return b/n;
	}
	
	public static FuzzySet inferere(List<FuzzySet> fss) throws Exception  {
		Domain d = fss.get(0).getDomain();
		
		for(FuzzySet fs : fss) {
			if(!fs.getDomain().equals(d)) {
				throw new Exception("Ne bi valjalo ako sve domene nisu iste");
			}
		}
		
		Map<String, Double> set = new HashMap<String, Double>();
		for(String key : d.getDomainComponents()) {
			double max = 0;
			for(FuzzySet fs : fss) {
				max = Math.max(max, fs.getMembershipFor(key));
			}
			set.put(key, max);
		}
		
		
		return new FuzzySet(d, set);
	}
	
}
