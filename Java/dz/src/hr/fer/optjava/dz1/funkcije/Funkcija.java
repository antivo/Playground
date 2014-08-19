package hr.fer.optjava.dz1.funkcije;

/**
 * 
 * Sučelje koje definira sve metode koje jedna funkcija, nad kojom se provodi
 * genetski algoritam, treba imati.
 * 
 * @author Eugen Rožić
 * @version 0.1
 *
 */
public interface Funkcija {
	
	/** konstanta koja označava minimiziranje funkcije*/
	public static int MIN=-1;
	/** konstanta koja označava maksimiziranje funkcije*/
	public static int MAX=1;

	/**
	 * Metoda koja bi trebala računati fitness. Fitness nužno ne mora odgovarati
	 * vrijednosti same funkcije. Npr. fitness može biti i negativna vrijednosti
	 * funkcije, ako se funkciju minimizira (jer je tako lakše sortirati
	 * jedinke)
	 * 
	 * @param vektor vrijednosti nad kojima se računa funkcija
	 * @return vrijednosti fitnessa
	 */
	public double doFitness(double[] vektor);
	
	/**
	 * Metoda koja računa stvarnu vrijednosti funkcije za dane vrijednosti
	 * 
	 * @param vektor vrijednosti nad kojima se računa funkcija
	 * @return vrijednost funkcije za dani vektor
	 */
	public double doRealValue(double[] vektor);
	
	
}
