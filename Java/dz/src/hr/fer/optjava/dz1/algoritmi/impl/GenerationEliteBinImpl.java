package hr.fer.optjava.dz1.algoritmi.impl;

import hr.fer.optjava.dz1.algoritmi.GenetskiAlgoritam;
import hr.fer.optjava.dz1.funkcije.Funkcija;
import hr.fer.optjava.dz1.funkcije.impl.RastriginovaFunkcija;
import hr.fer.optjava.dz1.model.Dekoder;
import hr.fer.optjava.dz1.model.JedinkaBinary;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * 
 * Razred koji implementira genetski algoritam za minimizaciju n-dimenzionalne
 * (rastriginove) funkcije.
 * Prikaz jedinki (kromosoma) je binarni, a algoritam je generacijski i elitistički.
 * Punjenje matingPool-a je čisto dvo-turnirsko, isto kao i križanje.
 * 
 * @author Eugen Rožić
 * @version 0.1
 *
 */
public class GenerationEliteBinImpl implements GenetskiAlgoritam {

	/**velicina populacije s kojom se radi*/
	private int velicinaPopulacije;
	/**referenca na primjerak razreda Dekoder.
	 * Služi za generiranje i dekodiranje genotipova jedinki */
	private Dekoder dekoder;
	/** referenca na primjerak razreda koji naslijeđuje sučelje Funkcija.
	 * Služi za izračunavanje funkcije nad kojom se provodi optimizacija. 
	 */
	private Funkcija funkcija;
	
	/**
	 * brojač generacija
	 */
	private int gen=0;
	
	/** vjerojatnost mutacije*/
	private double Pm;
	
	private List<JedinkaBinary> populacija;
	private List<JedinkaBinary> matingPool;
	private List<JedinkaBinary> djeca;
	
	/** generator slučajnih brojeva */
	private Random random = new Random(System.currentTimeMillis());
	
	/**
	 * Jedini konstruktor razreda. Postavlja sve parametre algoritma te instancira
	 * dekoder i funkciju
	 * 
	 * @param dimenzija dimenzionalnost funkcije
	 * @param donjaGranica donja granica pretrage (u svim dimenzijama)
	 * @param gornjaGranica gornja grancia pretrage (u svim dimenzijama)
	 */
	public GenerationEliteBinImpl(int dimenzija, int donjaGranica, int gornjaGranica) {
		
		this.dekoder=new Dekoder(dimenzija,donjaGranica,gornjaGranica);
		this.funkcija=new RastriginovaFunkcija(Funkcija.MIN);
				
		this.Pm=0.03; //3% - da ih malo razdrma, ali da ne uništi ono dobro dobiveno križanjem
		
		//TODO jel ima smisla toliko povećavat populaciju sa dimenzijom funkcije?
		//jer... to užasno usporava...a ionako je prostor pretraživanja beskonačan... ?
		//this.velicinaPopulacije=(gornjaGranica-donjaGranica)*dimenzija*2;
		this.velicinaPopulacije=50;
	}
	
	
	/**
	 * metoda za pokretanje algoritma...
	 */
	@Override
	public void start() {
		
		generirajPočetnuPopulaciju();
		evaluirajPopulaciju();
		System.out.println("Početna populacija:\n"+output(populacija));
		pause();
		
		while (true){
			//TODO treba li mi ovo?
			//random.setSeed(System.currentTimeMillis());
			gen++;
			System.out.println(gen+". generacija");
			
			napuniMatingPool();
			//System.out.println("matingPool:\n"+output(matingPool)+"\n");
			
			reprodukcija();
			
			evaluirajPopulaciju();			

			if (gen%100==0) {
				System.out.println("\n"+gen+". korak:\n"+output(populacija));
				pause();
			}
		}
		
	}
	
	/**
	 * Ova metoda uparuje jedinke iz "mating pool"-a, križa ih, mutira nastalu djecu
	 * i dodaje ih u listu djece sve dok količina djece ne dostigne dvostruku veličinu
	 * populacije.
	 */
	private void reprodukcija(){
		
		djeca=new ArrayList<JedinkaBinary>(2*velicinaPopulacije);
		
		while (djeca.size()<velicinaPopulacije*2){
			JedinkaBinary[] par=selekcija(matingPool);
			
			//System.out.println("\nprvi roditelj: "+par[0].toString());
			//System.out.println("prvi roditelj: "+par[1].toString());
			
			//uzme par roditelja i vrati par djece
			par=križanjeSJednomTočkomPrekida(par);
			
			//System.out.println("\nprvo dijete: "+par[0].toString());
			//System.out.println("drugo dijete: "+par[1].toString());
			
			djeca.add(mutacija(par[0]));
			djeca.add(mutacija(par[1]));
			
			//System.out.println("\nprvo mutirano dijete: "+par[0].toString());
			//System.out.println("drugo mutirano dijete: "+par[1].toString());
		}
	}
	
	/**
	 * Metoda koja križa par jedinki i vraća par djece.
	 * Radi na principu jedne točke prekida koja se nasumično izabere. Bitovi do točke prekida
	 * od prvog roditelja idu prvom djetetu i od drugog roditelja drugom djetetu, a iza
	 * točke prekid obrnuto. 
	 * 
	 * @param par par roditelja
	 * @return par djece
	 */
	private JedinkaBinary[] križanjeSJednomTočkomPrekida(JedinkaBinary[] par) {
		
		JedinkaBinary[] djeca=new JedinkaBinary[2];
		
		byte[] bitovi1 = new byte[dekoder.bitova_ukupno];
		byte[] bitovi2 = new byte[dekoder.bitova_ukupno];
		
		int tockaPrekida = random.nextInt(dekoder.bitova_ukupno-1)+1;
		for (int i=0 ; i<tockaPrekida ;i++){
			bitovi1[i]=(par[0].getBitovi())[i];
			bitovi2[i]=(par[1].getBitovi())[i];
		}
		for (int i=tockaPrekida; i<dekoder.bitova_ukupno ;i++){
			bitovi1[i]=(par[1].getBitovi())[i];
			bitovi2[i]=(par[0].getBitovi())[i];
		}
		
		djeca[0] = new JedinkaBinary(bitovi1);
		djeca[1] = new JedinkaBinary(bitovi2);
		
		return djeca;
	}
	
	/**
	 * Metoda koja mutira jednu jedinku.
	 * To radi na način da za svaki bit u svakoj dimenziji izgenerira broj između 0 i 1
	 * i ako je manji od Pm onda ga 'okrene'. 
	 * 
	 * @param jedinka ona koja treba biti mutirana
	 */
	private JedinkaBinary mutacija(JedinkaBinary jedinka) {
		
		for (int i=0 ; i<dekoder.bitova_ukupno ; i++){
				//TODO ovo malo puno košta...previše poziva na random
				//neznam kak bi to s manje a da ostane vjerodostojno...
				//možda da u svakoj dimneziji sa jednim randomom odlučim
				//koji jedan bit ću mijenjat? (al onda nema šanse da se promijeni >1, ali
				//niti da se ne promijeni niti jedan)
				double broj=random.nextDouble();
				if (broj<Pm) (jedinka.getBitovi())[i]^=(byte)1;
		}
		return jedinka;
	}
	
	/**
	 * Metoda koja poziva metodu selekcije 2 jedinke iz populacije i bolju
	 * stavlja u matingPool dok ne napuni MatingPool
	 * tj. dok MatingPool ne dostigne veličinu ternutne populacije.
	 */
	private void napuniMatingPool(){
		
		matingPool=new ArrayList<JedinkaBinary>(velicinaPopulacije);
		
		while(matingPool.size()<velicinaPopulacije){
			JedinkaBinary[] par=selekcija(populacija);
			if (par[0].getFitness()>par[1].getFitness()) matingPool.add(par[0]);
			else matingPool.add(par[1]);
		}
	}
	
	/**
	 * Metoda koja iz dane populacije(liste) nasumično bira 2 jedinke
	 * @return jedinka koja ima veći fitness 
	 */
	private JedinkaBinary[] selekcija(List<JedinkaBinary> populacija) {
		
		JedinkaBinary[] par=new JedinkaBinary[2];
		
		for (int i=0;i<2;i++){
			
			int index=random.nextInt(populacija.size());
			par[i]=populacija.get(index);
					
		}
		
		return par;
	}

	/**
	 * Metoda koja sortira populaciju, odbacuje sve osim prvih N najboljih i
	 * svakoj jedinki u populaciji pridjeljuje fitness.
	 */
	private void evaluirajPopulaciju() {
		
		//ovo poprilično košta čini mi se...
		if (gen>0)
			populacija.addAll(djeca);
		
		for (JedinkaBinary jed:populacija)
			if (jed.getFitness()==null)
				jed.setFitness(funkcija.doFitness(dekoder.decode(jed.getBitovi())));
		
		Collections.sort(populacija);
		
		//da ne radi bezveze na početnoj evaluaciji
		if (gen>0) populacija=populacija.subList(velicinaPopulacije*2, velicinaPopulacije*3);
		
	}

	/**
	 * Metoda koja generira početnu populaciju
	 * @return
	 */
	private void generirajPočetnuPopulaciju() {
		populacija=new ArrayList<JedinkaBinary>(velicinaPopulacije);
		byte[] bitovi;
		
		for (int i=0 ; i<velicinaPopulacije ; i++){
			bitovi=dekoder.generirajBitove(random);
			populacija.add(new JedinkaBinary(bitovi));	
		}
	}
	
	/**
	 * Ispis trenutne populacije
	 * @return
	 */
	private String output(List<JedinkaBinary> populacija){
		StringBuilder str=new StringBuilder();
		
		for (int i=0; i<populacija.size();i++){
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
