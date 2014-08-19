package hr.fer.optjava.dz1.algoritmi.impl;

import hr.fer.optjava.dz1.algoritmi.GenetskiAlgoritam;
import hr.fer.optjava.dz1.funkcije.Funkcija;
import hr.fer.optjava.dz1.funkcije.impl.RastriginovaFunkcija;
import hr.fer.optjava.dz1.model.JedinkaObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;

/**
 * * Razred koji implementira genetski algoritam za minimizaciju n-dimenzionalne
 * (rastriginove) funkcije.
 * Prikaz jedinki (kromosoma) je u objektima (realni brojevi), a algoritam
 * je steady-state i nije elitistički.
 * 
 * Izabir jedinki za križanje je hibridni 3-turnirski (dvije bolje se križaju,
 * dijete umjesto trećeg).
 * 
 * @author Eugen Rožić
 * @version 0.1
 *
 */
public class SteadyStateObjImpl implements GenetskiAlgoritam {
	
	/** dimenzionalnost funkcije */
	private int dimenzija;
	/** donja granica pretrage (u svim dimenzijama)*/
	private int donjaGranica;
	/** gornja granica pretrage (u svim dimenzijama)*/
	private int gornjaGranica;
	
	/**velicina populacije s kojom se radi*/
	private int velicinaPopulacije;
	
	/** referenca na primjerak razreda koji naslijeđuje sučelje Funkcija.
	 * Služi za izračunavanje funkcije nad kojom se provodi optimizacija. 
	 */
	private Funkcija funkcija;
	
	/** brojač koraka */
	private int korak=0;
	
	private List<JedinkaObject> populacija;
	
	/** generator slučajnih brojeva */
	private Random random = new Random(System.currentTimeMillis());
	
	/**
	 *  Jedini konstruktor razreda. Postavlja sve parametre algoritma.
	 *  
	 * @param dimenzija dimenzionalnost funkcije
	 * @param donjaGranica donja granica pretrage (u svim dimenzijama)
	 * @param gornjaGranica gornja grancia pretrage (u svim dimenzijama)
	 */
	public SteadyStateObjImpl(int dimenzija, int donjaGranica, int gornjaGranica) {
		this.dimenzija=dimenzija;
		this.donjaGranica=donjaGranica;
		this.gornjaGranica=gornjaGranica;
		
		//?
		//this.velicinaPopulacije=(gornjaGranica-donjaGranica)*dimenzija*2;
		this.velicinaPopulacije=50;
		
		this.funkcija=new RastriginovaFunkcija(Funkcija.MIN);
	}
	
	
	/**
	 * metoda za pokretanje algoritma...
	 */
	@Override
	public void start() {
		
		generirajPočetnuPopulaciju();
		evaluirajPopulaciju();
		Collections.sort(populacija);
		System.out.println("Početna populacija:\n"+outputPOP());
		pause();
		
		while (true){
			TreeSet<JedinkaObject> roditelji= selekcija();
			
			/*
			System.out.println("Izabrane su 3 jedinke:");
			for (JedinkaObject jed:roditelji){
				System.out.println(jed.toString());
			}
			*/
			
			JedinkaObject dijete=križaj(roditelji.pollLast(), roditelji.pollLast());
			//System.out.println("dijete: "+dijete.toString());
			
			mutiraj(dijete);
			
			populacija.remove(roditelji.pollFirst());
			populacija.add(dijete);
			korak++;
			evaluirajPopulaciju();
			//System.out.println("Mutirano dijete: "+dijete.toString());
			
			if (korak%1000==0) {
				Collections.sort(populacija);
				System.out.println("\n"+korak+". korak:\n"+outputPOP());
				pause();
			}
		}
		
		
	}
	
	/**
	 * Metoda koja mutira jednu jedinku.
	 * To radi na način da svakoj komponenti vektora predane jedinke
	 * dodajte slučajno generirani broj iz normalne razdiobe za parametrima (0,sigma^2).
	 * Sigma bi trebao biti takav da 12*sigma(6 na svaku stranu)
	 * bude otprilike gornja-donja granica. Tako se osigurava da će puno brojeva
	 * biti mali, ali će vjerojatnost da se napravi veći skok ipak postojati, no taj
	 * skok 'nikad' neće biti veći od (gornja-donja granica)/2 
	 * 
	 * Ako 'mutirani' broj ipak prijeđe granice ova metoda ga 'reže' za cijeli raspon
	 * (modulo raspon)
	 * 
	 * @param jedinka jedinka koja treba biti mutirana
	 */
	private void mutiraj(JedinkaObject jedinka) {
		
		double[] vrijednosti = jedinka.getVektorX(); 
		int raspon=(gornjaGranica-donjaGranica);
		
		for (int i=0; i<vrijednosti.length;i++){
			// jel ovo dobro?? mutacija mi je prevelika... nebi baš svaki broj mutirao
			//to mi se čini malo previše mutiranja...
			//zašto nebi, da ako lijepo izgleda da ne mutiramo... a ako je ružno da mutiramo ?
			//malo je problemski specifično al kaj onda?
			double broj=random.nextGaussian()*raspon/48;
			vrijednosti[i]+=broj;
			if (vrijednosti[i]>gornjaGranica) {
				vrijednosti[i]%=raspon;
				vrijednosti[i]+=donjaGranica;
			}
			else if (vrijednosti[i]<donjaGranica) {
				vrijednosti[i]%=(raspon);
				vrijednosti[i]+=gornjaGranica;
			}
		}
		
		jedinka.setVektorX(vrijednosti);
	}

	/**
	 * Metoda koja križa 2 jedinke i vraća dijete.
	 * To radi na principu aritmetičke sredine odgovarajućih komponenti vektora roditelja.
	 * 
	 * Znači dijete[Xi]=(roditelj[Xi]+roditelj[Xi])/2
	 * 
	 * @param roditelj1
	 * @param roditelj2
	 * @return dijete
	 */
	private JedinkaObject križaj(JedinkaObject roditelj1, JedinkaObject roditelj2) {
		
		double[] vrijednosti=new double[dimenzija];
		
		for (int i=0;i<dimenzija;i++){
			vrijednosti[i]=(roditelj1.getVektorX()[i]+roditelj2.getVektorX()[i])/2;
		}
		
		return new JedinkaObject(vrijednosti);
	}

	/**
	 * Metoda koja iz trenutne populacije nasumično bira 3 jedinke na osnovi njihovog
	 * fitness-a. Moraju biti različite! 
	 * @return treeset od 3 jednike (treeset je automatski sortiran) 
	 */
	private TreeSet<JedinkaObject> selekcija() {
		
		TreeSet<JedinkaObject> roditelji=new TreeSet<JedinkaObject>();
		
		while (roditelji.size()<3){
			int index=random.nextInt(populacija.size());
			roditelji.add(populacija.get(index));
		}
		
		return roditelji;
	}

	/**
	 * Metoda koja svakoj jedinki u populaciji pridjeljuje fitness na osnovi
	 * vrijednosti funkcije za fenotip te jedinke.
	 */
	private void evaluirajPopulaciju() {
		
		for (JedinkaObject jed:populacija)
			if (jed.getFitness()==null)
				jed.setFitness(funkcija.doFitness(jed.getVektorX()));
		
	}

	/**
	 * Metoda koja generira početnu populaciju
	 */
	private void generirajPočetnuPopulaciju() {
		
		populacija=new ArrayList<JedinkaObject>(velicinaPopulacije);
		double[] vrijednosti;
		
		for (int i=0 ; i<velicinaPopulacije ; i++){
			vrijednosti=new double[dimenzija];
			for (int j=0 ; j<dimenzija ; j++){
				vrijednosti[j]=random.nextDouble()*(gornjaGranica-donjaGranica) + donjaGranica;
			}
			populacija.add(new JedinkaObject(vrijednosti));	
		}
	}
	
	/**
	 * metoda koja priprema ispis trenutne generacije
	 * @return string koji sadrži lijepi zapis generacije
	 */
	private String outputPOP(){
		StringBuilder str=new StringBuilder();
		
		for (int i=0; i<velicinaPopulacije;i++){
			str.append((i+1)+"-ta jedinka: "+populacija.get(i).toString()+"\n");
		}
		
		return str.toString();
	}
	
	/**
	 * Pauzira dok korisnik ne stisne enter...
	 */
	public static void pause() {
		System.out.println("Press [enter] to continue");
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			reader.readLine();
		} catch (Exception e){
			e.printStackTrace();
		}
	}

}
