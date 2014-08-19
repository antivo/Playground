package hr.fer.zemris.fenc.definer;

abstract public class Definer {
	static protected String[] trim(String[] toTrim) {
		for(int i =  0; i < toTrim.length; ++i) {
			toTrim[i] = toTrim[i].trim();
		}
		
		return toTrim;
	}
}
