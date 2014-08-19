package hr.fer.optjava.dz1.model;


/**
 * 
 * Implementacija jedinke sa realnim brojevima.
 * Vrijednostima se manipulira matematičkim operacijama, a ne bitovno.
 * 
 * 
 * @author Eugen Rožić
 * @version 0.1
 *
 */
public class JedinkaBinary implements Comparable<JedinkaBinary> {

	/**
	 * sadrži sve komponente vrijednosti jedinke
	 */
	private byte[] bitovi;
	
	/**
	 * fitness jedinke = vrijednost dane funkcije
	 * (-rastrigin(x) za konkretni slučaj. Negativna vrijednost jer se minimizira.)
	 */
	private Double fitness=null;
	
	/**
	 * Konstruktor
	 * @param vrijednosti - vektor vrijednosti
	 */
	public JedinkaBinary(byte[] bitovi) {
		this.bitovi=bitovi;
	}
	
	/**
	 * Metoda koja vraća genotip jedinke
	 * @return polje bajtova "bitovi"
	 */
	public byte[] getBitovi() {
		return bitovi;
	}

	/**
	 * Metoda koja postavlja genotip jedinke
	 * @param bitovi bitovni genotip koji se postavlja
	 */
	public void setBitovi(byte[] bitovi) {
		this.bitovi=bitovi;
	}

	/**
	 * Metoda koja vraća fitness jedinke
	 * @return the fitness
	 */
	public Double getFitness() {
		return fitness;
	}

	/**
	 * Metoda kojom se postavlja fitness jedinke
	 * @param fitness the fitness to set
	 */
	public void setFitness(double fitness) {
		this.fitness = fitness;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj){
		if (!(obj instanceof JedinkaBinary)) return false;
		//TODO treba li implementirati hashCode il je ovaj od Objecta dovoljno dobar?
		
		//TODO trebalo bi napraviti hash koji se računa na osnovi vrijednosti (fitnessa)
		// i tako da se ne poziva metoda nego vrijednost neke varijable u koju se to spremi
		return obj.hashCode()==this.hashCode();
	}
	
	/**
	 * komparator funkcija za comparable sučelje.
	 * Uspoređuje dvije jedinke po njihovom fitnessu.
	 * {@inheritDoc}
	 */
	@Override
	public int compareTo(JedinkaBinary o) {
		if (this.fitness>o.fitness) return 1;
		else if (this.fitness<o.fitness) return -1;
		else return 0;
	}
	
	/**
	 * Metoda koja vraća lijepo ispisane sve informacije o jedinki
	 */
	@Override
	public String toString() {		
		
		StringBuilder str=new StringBuilder("fitness: "+fitness+" ,values: ");
				
		for (int i=0;i<bitovi.length;i++)
			str.append(bitovi[i]);
		
		return str.toString();
	}
	
}
