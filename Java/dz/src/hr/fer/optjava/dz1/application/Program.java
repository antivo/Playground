package hr.fer.optjava.dz1.application;

import hr.fer.optjava.dz1.algoritmi.GenetskiAlgoritam;
import hr.fer.optjava.dz1.algoritmi.impl.GenerationEliteBinImpl;
import hr.fer.optjava.dz1.algoritmi.impl.SteadyStateObjImpl;

/**
 * Ova klasa služi samo za prihvaćanje parametara komandne linije, 
 * stvaranje primjeraka razreda koji implementiraju genetske algoritme
 * te njihovo pokretanje u ovisnosti o pročitanim argumentima.
 * 
 * @author Eugen Rožić
 * @version 0.1
 * 
 *
 */
public class Program {

	/**
	 * Pokreće aplikaciju pozvanu iz komandne linije
	 * @param args argumenti iz komandne linije
	 */
	public static void main(String[] args) {
		
		int dimenzija=0;
		int donjaGranica=0;
		int gornjaGranica=0;
		
		if (args.length!=4) {
			System.err.println("Programu se trebaju predati 4 argumenta: dimenzionalnost" +
					" vektora, gornja i donja granica pretraživanja (za sve dimenzije ista) te" +
					"izabir algoritma (steady il gen).");
			System.exit(-1);
		}
		try{
			dimenzija=Integer.parseInt(args[0]);
			donjaGranica=Integer.parseInt(args[1]);
			gornjaGranica=Integer.parseInt(args[2]);
		} catch (NumberFormatException e){
			System.err.println("Uneseni brojevi moraju biti integeri!");
			System.exit(-1);
		} catch (Exception e2){
			System.err.println("Nepoznata greška...");
			System.exit(-1);
		}
		
		if (donjaGranica>gornjaGranica || dimenzija<=0) {
			System.err.println("Dimenzija mora biti >0 i gornja granica > donja granica!");
			System.exit(-1);
		}
		
		GenetskiAlgoritam algoritam=null;
		
		if (args[3].equalsIgnoreCase("steady"))
			algoritam = new SteadyStateObjImpl(dimenzija,donjaGranica,gornjaGranica);
		else if (args[3].equalsIgnoreCase("gen"))
			algoritam = new GenerationEliteBinImpl(dimenzija,donjaGranica,gornjaGranica);
		else {
			System.err.println("Nepostojeći algoritam!");
			System.exit(-1);
		}
			
		algoritam.start();
		

	}

}
