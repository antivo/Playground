package hr.fer.optjava.dz1.model;


/**
 * 
 * Implementacija jedinke sa realnim brojevima.
 * Vrijednostima se manipulira matematičkim operacijama, a ne bitovno.
 * 
 * @author Eugen Rožić
 * @version 0.1
 *
 */
public class JedinkaObject implements Comparable<JedinkaObject> {

	/**
	 * sadrži sve komponente vrijednosti jedinke
	 */
	private double[] vektorX;
	
	/**
	 * sadrži fitness jedinke unutar svoje populacije
	 */
	private Double fitness=null;
	
	/**
	 * Konstruktor
	 * @param vrijednosti - vektor vrijednosti
	 */
	public JedinkaObject(double[] vrijednosti) {
		vektorX=vrijednosti;
	}
	
	/**
	 * Metoda koja vraća fenotip jedinke
	 * @return vektorX fenotip jednike
	 */
	public double[] getVektorX() {
		return vektorX;
	}

	/**
	 * Metoda koja postavlja fenotip jednike
	 * @param vektorX fenotip koji se postavlja
	 */
	public void setVektorX(double[] vektorX) {
		this.vektorX = vektorX;
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
		if (!(obj instanceof JedinkaObject)) return false;
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
	public int compareTo(JedinkaObject o) {
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
				
		int i=0;
		for (i=0;i<vektorX.length-1;i++){
			str.append(vektorX[i]+", ");
		}
		str.append(vektorX[i]+".");
		
		return str.toString();
	}
	
}
