<?php
abstract class Baza {
	protected $db;
	
	public function __construct() {
		$this->db = null;
	}
	
	protected function spojiSe() {
		try {
			$this->db = new PDO('mysql:dbname=XXXXXXX', 'XXXXXXX', 'XXXXXXX',
			array(PDO::MYSQL_ATTR_INIT_COMMAND=>"SET NAMES utf8"));
			$this->db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
		} catch (PDOException $e) {
			//echo "Greška kod spajanja: " . $e-s>getMessage();
		}
	}
	
	protected function odspojiSe() {
		$this->db = null;
	}
}

class Evoker  extends Baza {
	public function __construct() {
		Baza::__construct();
	}
	
	public function invUlogiraj(&$user, &$pass) {
		$this->spojiSe();
		
		$upit = $this->db->prepare("SELECT `id`, `privilegije` FROM `dz5_korisnici` WHERE `username`=\"" . $user . "\" AND `lozinka`=\"" . $pass . "\"");
		$upit->setFetchMode(PDO::FETCH_NUM);
		$upit->execute();
			
		$this->odspojiSe();
		
		$rezult = null;
		$typeOfUser = null;
		
		foreach ($upit as $row){
			$rezult     = $row[0];
			$typeOfUser = $row[1];
			break;
		}
		
		if ($rezult === null)
			return 0; // NULA ZNACI GRESKU, zato se zove inv :P
		
		if ($typeOfUser == 'korisnik')
			return $rezult;

		switch ($typeOfUser) {
			case 'trgovac' : return -1;
			case 'admin'   : return -2;
		}
	}
}

class Painter extends Baza {
	public function __construct() {
		Baza::__construct();
	}
	
	public function showPicture(&$id, $h=84, $w=84) {
		return "<img src=\"iAmAPicture.php?id=" . $id .  "\" alt=\"Slika Artikla\" height=\"". $h ."\" width=\"" . $w . "\"  />";
	}
	
	public function getPicture(&$id) {
		$this->spojiSe();
		
		$upit = $this->db->prepare("SELECT `data`, `size` FROM `dz5_artikliSlike` WHERE `id`=" . $id);
		$upit->setFetchMode(PDO::FETCH_NUM);
		$upit->execute();
		
		$this->odspojiSe();
		
		$rez = array();
		
		foreach ($upit as $row) {
			$rez['data'] = $row[0];
			$rez['size'] = $row[1];
			return $rez;
		}
		
		return null;
	}
}

class Zapisnicar extends Baza {
	public function __construct() {
		Baza::__construct();
	}
	
	public function doesItExist(&$id) {
		$this->spojiSe();
		
		$upit = $this->db->prepare("SELECT `ime` FROM `dz5_korisnici` WHERE `id`=" . $id);
		$upit->setFetchMode(PDO::FETCH_NUM);
		$upit->execute();

		$this->odspojiSe();
		
		foreach ($upit as $row) 
				return true;
		
		return false;
	}
	
	public function prikaziKlijenta (&$id) {
		$this->spojiSe();
		
		$upit = $this->db->prepare("SELECT `ime`, `prezime`, `adresa` FROM `dz5_korisnici` WHERE `id`=" . $id);
		$upit->setFetchMode(PDO::FETCH_NUM);
		$upit->execute();
		
		$this->odspojiSe();
		
		foreach($upit as $row) 
			echo "Ime: " . $row[0] . "<br/>" . "Prezime: " . $row[1] . "<br/>" . "Adresa: " . $row[2] . "<br/><br/>" ;
	}
	
	public function izmjeniKlijenta(&$id, &$ime, &$prezime, &$adresa) {
		$this->spojiSe();
		
		if ($ime != '') {
			$update = $this->db->prepare("UPDATE `dz5_korisnici` SET `ime`=\"" . $ime ."\" WHERE `id`=" . $id);
			$update->execute();
		}
		
		if ($prezime != '') {
			$update = $this->db->prepare("UPDATE `dz5_korisnici` SET `prezime`=\"" . $prezime . "\" WHERE `id`=" . $id);
			$update->execute();
		}
		
		if ($adresa != '') {
			$update = $this->db->prepare("UPDATE `dz5_korisnici` SET `adresa`=\"" . $adresa . "\" WHERE `id`=" . $id);
			$update->execute();
		}
		
		$this->odspojiSe();
	}
	
	public function izbrisiKlijenta (&$id) {
		$this->spojiSe();
		
		$upit = $this->db->prepare("DELETE FROM `dz5_korisnici` WHERE `id`=" . $id);
		$upit->execute();
		
		$this->odspojiSe();
	}
	
	protected function pridjeliAkcije(&$id) {
		return "<a href=\"izmjeniKorisnika.php?id=" . $id . "\">Izmjeni podatke</a>&nbsp;&nbsp;<a href=\"prikaziKlijente.php?delete=" . $id . "\">DELETE</a>";
	}
	
	public function pregledajKorisnike() {
		$this->spojiSe();
		
		$upit = $this->db->prepare("SELECT `id`, `ime`, `prezime`, `adresa` FROM `dz5_korisnici`");
		$upit->setFetchMode(PDO::FETCH_NUM);
		$upit->execute();
		
		$this->odspojiSe();
		echo "<br/><table border=\"1\"><tr><td>Ime</td><td>Prezime</td><td>Adresa</td><td>Akcija</td></tr>";
			foreach($upit as $entry){ 
				$isNull = false;
				echo "<tr>";
				for ($i = 1; $i < 4; ++$i) 
					echo  "<td>" . $entry[$i] . "</td>";
				echo  "<td>" .  $this->pridjeliAkcije($entry[0]) . "</td>";
				echo "</tr>";
			}
			echo "<table><br/>" ;
		
	}
	
	public function registerUser(&$username, &$lozinka, &$ime, &$prezime, &$adresa, &$privilegije='korisnik', &$zakljucanaKosarica = 0) {
		$this->spojiSe();

		try {
		$insert = $this->db->prepare("INSERT INTO `dz5_korisnici` (`username`, `lozinka`, `ime`, `prezime`, `adresa`, `zakljucanaKosarica`, `privilegije`) VALUES (:username, :lozinka, :ime, :prezime, :adresa, :zakljucanaKosarica, :privilegije)"); 
		$insert->execute(array(":username" => $username, ":lozinka" => $lozinka, ":ime" => $ime, ":prezime" => $prezime, ":adresa" => $adresa , ":zakljucanaKosarica" => $zakljucanaKosarica, ":privilegije" => $privilegije));
		} catch (PDOException $e) {
			$this->odspojiSe();
			
			$whatIsTheProblem = $e->getMessage();
			// vrati -1 ako je userUNIQ skršen
			if(!(stristr($whatIsTheProblem, "'userUNIQ'") === FALSE)) 
				return -1;
				
			$whatIsTheProblem = $e->getMessage();
			//vrati -2 ako je personUNIQ
			if(!(stristr($whatIsTheProblem, "'personUNIQ'") === FALSE))
				return -2;
			
			//vrati -3 ako je ikakva druga greska
			return -3;
		}
		
		$this->odspojiSe();
		
		return 0;
	}
}

abstract class CommonBaza extends Baza {
	public function __construct() {
		Baza::__construct();
	}

	protected function searchByName(&$name) {
		if ($name === null)
			return null;
	
		$this->spojiSe();
		
		$upit = $this->db->prepare("SELECT `id` FROM `dz5_artikli` WHERE `naziv` LIKE '%" . $name . "%'");
		$upit->setFetchMode(PDO::FETCH_NUM);
		$upit->execute();
		
		$this->odspojiSe();
		
		$res = array();
		
		foreach ($upit as $row) 
			$res[] = $row[0];
		
		return $res;
	}
	
	abstract protected function addActions(&$i, &$collectedResults);
	
	protected function dodajSlikeArtiklima(&$line, &$collectedResults, $idArtikla) {
		$this->spojiSe();
		
		$upit = $this->db->prepare("SELECT `id` FROM `dz5_artikliSlike` WHERE `idArtikla`=" . $idArtikla);
		$upit->setFetchMode(PDO::FETCH_NUM);
		$upit->execute();
		
		$this->odspojiSe();
		
		$res = null;
		
		foreach ($upit as $row) {
			$res = $row[0];
			break;
		}
			
		$collectedResults[$line][] = $res;
		
		$collectedResults[$line][] = $idArtikla;
	}
	
	public function skupiArtikle(&$name = null) {
		$collectedKeys = CommonBaza::searchByName($name);
	
		$this->spojiSe();
		
		$upit = $this->db->prepare("SELECT `id`, `naziv`, `opis`, `cijena` FROM `dz5_artikli`");
		$upit->setFetchMode(PDO::FETCH_NUM);
		$upit->execute();
		
		$this->odspojiSe();
	
		$collectedResults = array();
		$line = -1;
		
		foreach ($upit as $row) {
			if ($name != null && in_array($row[0], $collectedKeys) === false)
				continue;
			
			$line++;
			$collectedResults[$line] = array();
			
			for($i = 1; $i < 4; ++$i) 
				if($i === 2)
					$collectedResults[$line][] = substr($row[$i],0,20);
				else
					$collectedResults[$line][] = $row[$i];
			
			$this->dodajSlikeArtiklima($line, $collectedResults, $row[0]);
			
			$this->addActions(&$line, &$collectedResults);
		}
		
		if ($line === -1)
			return null;
			
		return $collectedResults;
	}
}

class Guest extends CommonBaza {
	public function __construct() {
		CommonBaza::__construct();
	}
	
	protected function addActions(&$i, &$collectedResults) {}
}

class User extends CommonBaza {
	protected $id;
	
	public function __construct($idUser) {
		CommonBaza::__construct();
		$this->id = $idUser;
	}
	
	protected function addActions(&$i, &$collectedResults) {
		$collectedResults[$i][] =  "Add to Cart";
	}
	
	protected function skupiSadrzajKosarice() {
		$rez = array();
		
		$this->spojiSe();
		
		$upit = $this->db->prepare("SELECT `naziv`, `cijena`, `komada` FROM `dz5_artikli` JOIN `dz5_kosarica` ON `dz5_artikli`.`id` = `dz5_kosarica`.`idArtikla` WHERE `idKorisnika`=" . $this->id);
		$upit->setFetchMode(PDO::FETCH_NUM);
		$upit->execute();

		$this->odspojiSe();
		
		foreach ($upit as $row) {
			$line = array();
			foreach($row as $cell)
					$line[] = $cell;
			$rez[] = $line;
		}
		
		if (empty($rez))
			return null;
		else
			return $rez;
		
	}
	
	public function prikaziKosaricu() {
		$a = $this->skupiSadrzajKosarice();
		if ($a === null) {
			echo "Niste narucili jos ni jedan jedini jedincati proizvod";
		}
		else {
			$total = 0;
			echo "<br/><table border=\"1\"><tr><td>Naziv</td><td>Cijena</td><td>Komada</td></tr>";
			foreach($a as $entry){ 
				echo "<tr>";
				$j = 0;
				$sum = 0;
				foreach ($entry as $cell) {
						if ($j === 1)
							$sum = $cell;
						if ($j === 2)
							$total += $sum * $cell;
						echo  "<td>" . $cell . "</td>";
						++$j;
				}
				echo "</tr>";
			}
			echo "<table><br/>" . "TOTAL: " . $total . " kn<br><a href=\"prikaziKosaricu.php?odluka=kupi\">Kupi</a>&nbsp;&nbsp;<a href=\"prikaziKosaricu.php?odluka=odbaci\">Odbaci</a><br/>";
		}
		
	}
	
	public function haveIOrdered() {
		$this->spojiSe();
		
		$upit = $this->db->prepare("SELECT `zakljucanaKosarica` FROM `dz5_korisnici` WHERE `id`=" . $this->id);
		$upit->setFetchMode(PDO::FETCH_NUM);
		$upit->execute();

		$this->odspojiSe();
		
		foreach ($upit as $row) 
			if ($row[0] == 1)
				return true;

		return false;
	}
	
	protected function haveIBought(&$idArtikla) {
		$this->spojiSe();
		
		$upit = $this->db->prepare("SELECT `id` FROM `dz5_kosarica` WHERE `idArtikla`=" . $idArtikla . " AND `idKorisnika`=" . $this->id);
		$upit->setFetchMode(PDO::FETCH_NUM);
		$upit->execute();
		
		$this->odspojiSe();
		
		foreach ($upit as $row) {
			return $row[0];
		}
		
		return -1;
	}

	public function addToCart(&$idArtikla, &$quantity = 1) {
		if($this->haveIOrdered() === true)
			return false; 

		if($quantity < 1)
			return false;
		
		$cart = $this->haveIBought($idArtikla);
		$this->spojiSe();
		if ($cart == -1) {
			$insert = $this->db->prepare("INSERT INTO `dz5_kosarica` (`idArtikla`, `idKorisnika`, `komada`) VALUES (" . $idArtikla . ",\"" . $this->id . "\",\"" . $quantity . "\")"); 
			$insert->execute();
		}
		else {
				$update = $this->db->prepare("UPDATE `dz5_kosarica` SET `komada`=`komada` + " . $quantity . " WHERE `id`=" . $cart);
				$update->execute();
		}
	
		$this->odspojiSe();
		return true;
	}
	
	public function reject() {
		$this->spojiSe();
		
		$upit = $this->db->prepare("DELETE FROM `dz5_kosarica` WHERE `idKorisnika`=" . $this->id);
		$upit->execute();
		
		$this->odspojiSe();
	}
	
	public function order() {
		if($this->haveIOrdered() === true)
			return; // BACI EXCEPTION ILI IGNORIRAJ

		$this->spojiSe();
	
		$update = $this->db->prepare("UPDATE `dz5_korisnici` SET `zakljucanaKosarica`=1 WHERE `id`=" . $this->id);
		$update->execute();
		
		$this->odspojiSe();
	}
}

class Trader extends CommonBaza { 
	public function __construct() {
		CommonBaza::__construct();
	}
	
	public function procesuirajKosaricu(&$idKorisnika) {
		$this->spojiSe();
		
		$delete = $this->db->prepare("DELETE FROM `dz5_kosarica` WHERE `idKorisnika`=" . $idKorisnika);
		$delete->execute();
		
		$update = $this->db->prepare("UPDATE `dz5_korisnici` SET `zakljucanaKosarica`=0 WHERE `id`=" . $idKorisnika);
		$update->execute();
		
		$this->odspojiSe();
	}
	
	protected function skupiNarucitelje() {
		$rez = array();
		
		$this->spojiSe();
		
		$upit = $this->db->prepare("SELECT `id` FROM `dz5_korisnici` WHERE `zakljucanaKosarica`=1");
		$upit->setFetchMode(PDO::FETCH_NUM);
		$upit->execute();
		
		$this->odspojiSe();
		
		foreach($upit as $row) 
			foreach($row as $cell)
				$rez[] = $cell;
		
		if (empty($rez))
			return null;
		else
			return $rez;
	}
	
	protected function skupiKosarice() {
		$narucitelji = $this->skupiNarucitelje();
		if ($narucitelji === null)
			return null;
		else {
			$masterRez = array();
		
			foreach ($narucitelji as $idKorisnika) {
				$this->spojiSe();
				
				$upitSadrzaja = $this->db->prepare("SELECT `naziv`, `cijena`, `komada` FROM `dz5_kosarica` JOIN `dz5_artikli`  ON `dz5_artikli`.`id` = `dz5_kosarica`.`idArtikla` WHERE `idKorisnika`=" . $idKorisnika);
				$upitSadrzaja->setFetchMode(PDO::FETCH_NUM);
				$upitSadrzaja->execute();
				
				$upitPodataka = $this->db->prepare("SELECT `ime`, `prezime`, `adresa` FROM `dz5_korisnici` WHERE `id`=" . $idKorisnika);
				$upitPodataka->setFetchMode(PDO::FETCH_NUM);
				$upitPodataka->execute();
	
				$this->odspojiSe();
		
				$total = 0;
				$sadrzaj = array();
				foreach($upitSadrzaja as $row) {
					$j = 0;
					$sum = 0;
					$line = array();
					foreach($row as $cell) {
						$line[] = $cell;
						if ($j === 1)
							$sum = $cell;
						if ($j === 2)
							$total += $sum * $cell;
						++$j;
					}
					$sadrzaj[] = $line;
				}
				
				$rez['sadrzaj'] = $sadrzaj;
				$rez['total'] = $total;
		
				$podataka = array();
				foreach ($upitPodataka as $row) 
					foreach($row as $cell)
						$podataka[] = $cell;
				$rez['podataka'] = $podataka;
				
				$rez['id'] = $idKorisnika;
				
				$masterRez[] = $rez;
			}
			
			return $masterRez;
		}
	}
	
	protected function prikaziSadrzajKosarice(&$a) {
		$rez = "<table border=\"1\"><tr><td>Naziv Artikla</td><td>Cijena</td><td>Komada</td></tr>";
		foreach($a as $entry){ 
			$rez = $rez . "<tr>";

			foreach ($entry as $cell) 
				$rez = $rez .  "<td>" . $cell . "</td>";

			$rez = $rez . "</tr>";
		}
		$rez = $rez . "</table>";
		
		return $rez;
	}
	
	protected function prikaziPodatkeKosarice(&$a) {
		$rez = "Ime: " . $a[0] . "<br/>" . "Prezime: " . $a[1] . "<br/>" . "Adresa: " . $a[2];

		return $rez;
	}
	
	public function pregledajKosarice() {
		$a = $this->skupiKosarice();
		if ($a === null) {
			echo "Nema neobrađenih naručenih košarica.<br/><br/>";
		}
		else {
			echo "<br/><table border=\"1\"><tr><td>Sadrzaj kosarice</td><td>Vrijednost kosarice</td><td>Podatci narucitelja</td><td>Akcija</td></tr>";
			foreach($a as $entry){ 
				echo "<tr>";
				$j = 0;
				foreach ($entry as $cell) {
					switch ($j) {
						case 0 	 : echo  "<td>" . $this->prikaziSadrzajKosarice($cell) . "</td>";
								   break;
						case 1   : echo  "<td>" . $cell . " kn</td>";
								   break;
						case 2   : echo  "<td>" . $this->prikaziPodatkeKosarice($cell) . "</td>";
						           break;
						case 3   : echo  "<td><a href=\"obradiKosarice.php?obradi=" . $cell . "\">Obradi</a></td>";
								   break;
					}
					++$j;
				}
				echo "</tr>";
			}
			echo "<table><br/>";
		}
	}
	
	public function dodajArtikl(&$naziv, &$opis, &$cijena) {
		$this->spojiSe();
		
		try {
		$insert = $this->db->prepare("INSERT INTO `dz5_artikli` (`naziv`, `opis`, `cijena`) VALUES (:naziv, :opis, :cijena)"); 
		$insert->execute(array(":naziv" => $naziv, ":opis" => $opis, ":cijena" => $cijena));
		} catch (PDOException $e) {
			$this->odspojiSe();
			
			$whatIsTheProblem = $e->getMessage();
			// vrati -1 ako je artUNIQ skršen
			if(!(stristr($whatIsTheProblem, "'artUNIQ'") === FALSE)) 
				return -1;
			
			//vrati -2 ako je ikakva druga greska
			return -2;
		}
		
		$this->odspojiSe();
		
		return 0;
	}
	
	public function izbrisiArtikl(&$id) {
		$this->spojiSe();
		
		$upit = $this->db->prepare("DELETE FROM `dz5_artikli` WHERE `id`=" . $id);
		$upit->execute();
		
		$this->odspojiSe();
	}
	
	public function doesItExist(&$idArtikla) {
		$this->spojiSe();
		
		$upit = $this->db->prepare("SELECT `naziv` FROM `dz5_artikli` WHERE `id`=" . $idArtikla);
		$upit->setFetchMode(PDO::FETCH_NUM);
		$upit->execute();

		$this->odspojiSe();
		
		foreach ($upit as $row) 
				return true;
		
		return false;
	}
	
	public function getName(&$idArtikla) { // a ovo sam nabrzinu samo kopira funkciju iznad, da ne minjam sav dosadasnji kod napravit cu ovu
		$this->spojiSe();
		
		$upit = $this->db->prepare("SELECT `naziv` FROM `dz5_artikli` WHERE `id`=" . $idArtikla);
		$upit->setFetchMode(PDO::FETCH_NUM);
		$upit->execute();

		$this->odspojiSe();
		
		foreach ($upit as $row) 
				return $row[0];
		
		return '';
	}
	
	protected function addActions(&$i, &$collectedResults) {
		$collectedResults[$i][] =  "Uredi";
	}
	
	public function dodajSliku(&$idArtikla, &$mimeType, &$data, &$size) {
		$this->spojiSe();
		try {
		$insert = $this->db->prepare("INSERT INTO `dz5_artikliSlike` (`idArtikla`, `mimeType`, `data`, `size`) VALUES (:idArtikla, :mimeType, :data, :size)"); 
		$insert->execute(array(":idArtikla" => $idArtikla, ":mimeType" => $mimeType, ":data" => $data, ":size" => $size));
		} catch (PDOException $e) {
			$this->odspojiSe();
			//echo $e->getMessage();
			return -1;
		}
		
		return 0;
	}
	
	protected function collectPicutres(&$idArtikla) {
		$rez = array();
		
		$this->spojiSe();
		
		$upit = $this->db->prepare("SELECT `id` FROM `dz5_artikliSlike` WHERE `idArtikla`=" . $idArtikla);
		$upit->setFetchMode(PDO::FETCH_NUM);
		$upit->execute();

		$this->odspojiSe();
		
		foreach ($upit as $row) 
				$rez[] = $row[0];
		
		if (empty($rez))
			return null;
		else
			return $rez;
	}
	
	public function ispisiOsnovnePodatke(&$idArtikla) {
		$this->spojiSe();
		
		$upit = $this->db->prepare("SELECT `naziv`, `opis`, `cijena` FROM `dz5_artikli` WHERE `id`=" . $idArtikla);
		$upit->setFetchMode(PDO::FETCH_NUM);
		$upit->execute();
		
		$this->odspojiSe();
		
		foreach($upit as $row) {
			echo "Naziv: " . $row[0] . "<br/>";
			echo "Cijena: " . $row[2] . " kn<br/><br/>";
			echo "Opis: " . $row[1] . "<br/><br>";
		}
	} 
	
	public function tryToSell(&$idArtikla) {
		$this->ispisiOsnovnePodatke($idArtikla);

		$pictures = $this->collectPicutres($idArtikla);
		
		if ($pictures === null) {
			echo "(Proizvod nema priloženih slika.)<br/>";
			return;
		}
		
		echo "Slike:<br/>";
		
		$painter = new Painter();
		$k=0;
		foreach($pictures as $onePicture) {
			if($k === 0)
				echo "<p>";
			++$k;
			echo $painter->showPicture($onePicture, 150, 150) . "&nbsp&nbsp&nbsp&nbsp";
			if($k === 4)
				echo "</p>";
		}
	}
	
	public function ponudiSlike(&$idArtikla) {
		$pictures = $this->collectPicutres($idArtikla);
		if ($pictures === null)
			return;
		
		$painter = new Painter();
		
		echo "<br/><table border=\"1\"><tr><td>Slika</td><td>Akcija</td></tr>";
			
		foreach($pictures as $onePicture) 
			echo "<tr><td>" . $painter->showPicture($onePicture, 75, 75) . "</td><td><a href=\"izmjeniArtikl.php?update=" . $idArtikla . "&delete=" . $onePicture . "\">DELETE</a></td></tr>";
		echo "<table><br/>" ;
	}
	
	public function izmjeniArtikl(&$id, &$naziv, &$opis, &$cijena) {
		$this->spojiSe();
		
		if ($naziv != '') {
			$update = $this->db->prepare("UPDATE `dz5_artikli` SET `naziv`=\"" . $naziv ."\" WHERE `id`=" . $id);
			$update->execute();
		}
		
		if ($opis != '') {
			$update = $this->db->prepare("UPDATE `dz5_artikli` SET `opis`=\"" . $opis . "\" WHERE `id`=" . $id);
			$update->execute();
		}
		
		if ($cijena != '') {
			
			$update = $this->db->prepare("UPDATE `dz5_artikli` SET `cijena`=\"" . $cijena . "\" WHERE `id`=" . $id);
			$update->execute();
		}
		
		$this->odspojiSe();
	}
	
	public function obrisiSliku($idArtikla, $id) {
		$this->spojiSe();
		
		$upit = $this->db->prepare("DELETE FROM `dz5_artikliSlike` WHERE `id`=" . $id . " AND `idArtikla`=" . $idArtikla);
		$upit->execute();
		
		$this->odspojiSe();
	}
}
?>
