package hr.fer.optjava.dz1.funkcije.impl;

import hr.fer.optjava.dz1.funkcije.Funkcija;

/**
 * 
 * Razred koji implementira računanje Rastriginove funkciju nad proizvoljno velikim
 * vektorom vrijednosti (koliko vrijednosti toliko dimenzija)
 * 
 * @author Eugen Rožić
 * @version 0.1
 *
 */
public class RastriginovaFunkcija implements Funkcija{
	
	/**varijable koja čuva informaciju o 'korištenju' funkcije.
	 * Uvjetuje izlaz doFitness metode ovisno o tome je li namjena
	 * genetskog algoritma da minimizira ili maksimizira ovu funkciju.
	 */
	private int minmax;
	
	/**
	 * Jedini konstruktor ovog razreda.
	 * 
	 * @param minmax informacija o namjeni funkcije. Sprema se u privatnu varijablu.
	 */
	public RastriginovaFunkcija(int minmax) {
		if (minmax!=MIN && minmax!=MAX)
			throw new IllegalArgumentException("argument mora biti jedna od konstanti razreda!");
		
		this.minmax=minmax;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public double doFitness(double[] vektor) {
		
		return minmax*doRealValue(vektor);
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double doRealValue(double[] vektor) {
		
		double value=10*vektor.length;
		for (int i=0; i<vektor.length;i++){
			value+=vektor[i]*vektor[i] - (10*Math.cos(2*Math.PI*vektor[i]));
		}
		
		return value;
	}
	
	

}
