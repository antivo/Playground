#include <fstream>
#include <iostream>
#include <vector>
#include <map>
#include <set>
using namespace std;
struct Kljuc {
	public:
		string stanje;
		char znak;
		char znakP;	
	bool operator<(const Kljuc& arg)const {
		if (stanje != arg.stanje)
			return stanje < arg.stanje;
		if (znak != arg.znak)
			return znak < arg.znak;
		return znakP < arg.znakP;
	}
};
struct Prijelaz {
	public:
		string stanje;
		char znak;
		char znakP;
                char pomak;
		char pomakP;	
	bool operator<(const Prijelaz& arg)const {
		if (stanje != arg.stanje)
			return stanje < arg.stanje;
		if (znak != arg.znak)
			return znak < arg.znak;
		if (znakP != arg.znakP)
			return znakP < arg.znakP;
		if (pomak != arg.pomak)
			return pomak < arg.pomak;
		return pomakP < arg.pomakP;
	}
};
void StringExplode(string str, string separator, vector<string>* results) {
        int found = str.find_first_of(separator);;
        while(found != string::npos) {
                if(found > 0)
                        results->push_back(str.substr(0, found));
                str = str.substr(found+1);
                found = str.find_first_of(separator);
        }
        if(str.length() > 0)
                results->push_back(str);
}
typedef map<Kljuc, Prijelaz> delta;
int main () {
        /* procitaj definiciju iz datototeke def.txt*/   
        ifstream infile;
        string str;
        infile.open ("def.txt"); 
        if(infile.good()) getline(infile, str); // stanja
        if(infile.good()) getline(infile, str); // znakovi trake
        if(infile.good()) getline(infile, str); // znak za prazninu
        const char prazanZnak = str[0];         
        if(infile.good()) getline(infile, str); // pocento stanje
        string stanje = str;      
        /* ukoliko se citalo iz datoteke i ako se moze jos bar jednom -> DOBRO JE*/
        if(!infile.good()) { 
                cerr << "Osnovni sadzaj datoteke se nije ucitao";
                return 0;
                }
        if(infile.good()) getline(infile, str); //prihvatljivo stanje
        const string prih = str;
        
        vector<string> v, v2;
        Kljuc a;
        Prijelaz b;
        delta mapa;
        set<Kljuc> *mapa_prosirenja = new set<Kljuc>; // !! mnogo elemenata + heap je spor
        while(infile.good()) {             
                getline(infile, str);           //funkcija prijelaza
                StringExplode(str, "-", &v);    // !! desna strana sadrzi '>stanje,...'
                StringExplode(v[0], ",", &v2);
                a.stanje = v2[0];
                a.znak = v2[1][0];
                a.znakP = v2[2][0];
                v2.clear();
                StringExplode(v[1], ",", &v2);
                v2[0].erase(0,1);              //brise '>' na pocetku desne strane
                b.stanje = v2[0];
                b.znak = v2[1][0];
                b.znakP = v2[2][0];
                b.pomak = v2[3][0];
                b.pomakP = v2[4][0];
                v2.clear();             
                v.clear();
                /* gomilu kljuceva koji imaju prijelaz (qW,.,W,R,R) u skup*/
                if (b.stanje == "qW" && b.znak == prazanZnak && b.znakP == 'W' && b.pomak == 'R' && b.pomakP == 'R')
                        mapa_prosirenja->insert(a);
                else
                        mapa.insert(pair<Kljuc, Prijelaz>(a, b));
        }
        infile.close();
        str.clear();
        /* rad Turingovog stroja */     //@mapa, @prazanZnak, @stanje, @prih 
        /* pripremi trake */
        ifstream in;
        string ulaz="";
        in.open ("ulazni.txt");       
        while(in.good()) {              /*AKO SE ULAZNI NIZ NALAZI U VISE LINIJA*/
                getline(in, str);       /* TREBA POCISTIT PRAZNINE */
                str.erase(remove_if(str.begin(),str.end(),
                        static_cast<int(*)(int)>( isspace )),str.end());
                ulaz += str; 
                
        }
        in.close();
        str.clear();
        int duljina_ulaznog_niza = ulaz.length(); // za ispis na kraju programa
        cout << "Ulazni niz:" << endl << ulaz << endl << endl;
        ulaz = ulaz + prazanZnak + prazanZnak;
        int pozicija = 0;
        system("PAUSE");      
        cout << endl << "Izgled traka prije pocetka !"  << endl 
                << "Ulazna traka:" << endl << ulaz << endl << endl;
        
        string trakaP="";
        trakaP = trakaP + '<' + prazanZnak + prazanZnak;
        int pozicijaP = 1;
        int velicina = 2;
        cout << "Radna traka:" << endl << trakaP << endl << endl;
        
        /* simuliraj prijelaze */       
        a.stanje = stanje;
        a.znak = ulaz[pozicija];
        a.znakP = trakaP[pozicijaP];
        if (!mapa.count(a) && !mapa_prosirenja->count(a))
                cout << "Nema prijelaza" << endl << endl;
        int t = 0; // za zavrsni ispis, kolicina prijelaza
        while(1) { 
                if (mapa.count(a))
                        b = mapa[a];
                else if(mapa_prosirenja->count(a)) {
                        b.stanje = "qW"; 
                        b.znak = prazanZnak;
                        b.znakP = 'W';
                        b.pomak = 'R'; 
                        b.pomakP = 'R';
                }
                else break; /* izadji iz petlje */
                ++t;
                system("PAUSE");
                cout << endl << "Prijelaz: \t" << '(' << stanje << ',' << a.znak << ',' 
                        << a.znakP << ')' << "->";
                cout << '(' << b.stanje << ',' << b.znak << ',' << b.znakP << ',' 
                        << b.pomak << ',' << b.pomakP << ')' << endl << endl; 
                                       
              
                cout << "Ulazna traka:" << endl;
                for (int i = 0; i < (pozicija % 80); ++i)
                        cout << " ";
                cout << "|" << endl;
                cout << ulaz << endl << endl;
                
                cout << "Radna traka:" << endl;
                for (int i = 0; i < (pozicijaP % 80); ++i)
                        cout << " ";
                cout << "|" << endl;
                cout << trakaP << endl << endl;
                cout << "===============================" << endl; 
                
                stanje = b.stanje;
                ulaz[pozicija] = b.znak;
                trakaP[pozicijaP] = b.znakP;
                
                if(b.pomak == 'R')
                        ++pozicija;     // niz se nikada ne povecava !
                else if (b.pomak == 'L')
                        --pozicija;  
                                       
                if(b.pomakP == 'R') {
                        if(velicina < pozicijaP + 2) {
                                trakaP += prazanZnak;
                                ++velicina; 
                        }
                        ++pozicijaP;
                }
                else if (b.pomakP == 'L')
                        --pozicijaP;
                        
                a.stanje = stanje;
                a.znak = ulaz[pozicija];
                a.znakP = trakaP[pozicijaP];
        }
        delete mapa_prosirenja;       
        if( stanje == prih )    //samo je jedno prihvatljivo stanje
                cout << "HTML tablica je ispravna !!" << endl;
        else
                cout << "HTML tablica nije ispravna !!" << endl;
        cout << "Ulazni niz koji ste ucitali je bio duljine " << duljina_ulaznog_niza;
        cout << ". Ukupan broj prijelaza je " << t << '.' << endl;
        cout << "Zavrsetak programa. Nadam se da ste uzivali. Srdacan pozdrav." << endl; 
        system("PAUSE");       
        return 0;
}
