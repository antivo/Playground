package hr.fer.optjava.dz1.model;

import java.util.Random;

/**
 *  
 * Razred koji drži sve informacije i nudi sve potrebne metode za dekodiranje
 * jedinki. Pod dekodiranje se misli izračun stvarne vrijednosti za zadanu
 * funkciju iz polja bitova koje predstavlja genotip jednike.
 * 
 * @author Eugen Rožić
 * @version 0.1
 *
 */
public class Dekoder {
	
	/** broj bitova potrebnih za prikaz vrijednosti u jednoj dimenziji*/
	public int n;
	/**dimenzionalnost funkcije */
	public int dimenzija;
	/** ukupno bitova=n*dimenzija. Da se ne mora računati puno puta */
	public int bitova_ukupno;
	
	/** donja granica pretrage */
	public int donjaGranica;
	/** gornja granica pretrage */
	public int gornjaGranica;
	
	/**
	 * Jedini konstruktor razreda. Sprema predane argumente u privatne varijable
	 * razreda te izračunava broj potrebnih bitova za prikaz vrijednosti u jednoj
	 * dimenziji (n).
	 * 
	 * @param dimenzija dimenzionlanost funkcije
	 * @param donjaGranica donja granica pretrage
	 * @param gornjaGranica gornja granica pretrage
	 */
	public Dekoder(int dimenzija, int donjaGranica, int gornjaGranica){
		this.dimenzija=dimenzija;
		this.donjaGranica=donjaGranica;
		this.gornjaGranica=gornjaGranica;
		
		this.n=(int)Math.ceil(Math.log((gornjaGranica-donjaGranica)/Math.pow(10,-4))/Math.log(2));
		
		this.bitova_ukupno=n*dimenzija;
	}
	
	/**
	 * Pretvara genotip jedinke u polje double vrijednosti veličine broja dimenzija
	 * 
	 * @param bitovi genotip jedinke
	 * @return 'vektor' realnih vrijednosti
	 */
	public double[] decode(byte[] bitovi){
		
		double[] vrijednosti= new double[dimenzija];
		
		for (int i=0; i<dimenzija; i++) {
			
			for (int j=0; j<n; j++)
				vrijednosti[i]+=bitovi[j+i*n]*Math.pow(2,n-1-j);
			
			vrijednosti[i]=donjaGranica + 
				(vrijednosti[i]/(Math.pow(2,n)-1))*(gornjaGranica-donjaGranica);
		}
		
		return vrijednosti;
	}
	
	/**
	 * Stvara novo polje bajtova od kojih svaki predstavlja jedan bit
	 * u genotpiu jedne jedinke. Bitovi (tj. bajtovi) se stvaraju nasumično
	 * pomoći primjerka razreda Random koji je predan kao argument.
	 * @param random primjerak razreda Random
	 * @return polje bajtova (genotip jednike)
	 */
	public byte[] generirajBitove(Random random){
		
		byte[] bitovi=new byte[bitova_ukupno];
		
		for (int i=0; i<bitova_ukupno; i++){
			if (random.nextBoolean())
				bitovi[i]=1;
			else 
				bitovi[i]=0;
		}
		
		return bitovi;
	}

}
