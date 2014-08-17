<?php
/* admin
   username: fly
   pass: raid
*/

session_name(sha1("neznam treba li mi ovo"));
session_start();
//unset($_COOKIE['PHPSESSID']); 




if(!isset($_SESSION['CLIENT'])) { // JUST A GUEST
	unset($_SESSION);
	session_destroy(); 
}

if(isset($_SESSION['CLIENT'])) 
	$klijent = $_SESSION['CLIENT'];
else
	$klijent = 0;
	
?>
<html>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<head><title> Web shoPP </title></head>
<body>
	<?php
	if ($klijent === 0)
		echo "<form name=\"ulogirajSe\" action=\"rezultatiPrijave.php\" method=\"post\">
			<h4>Prijavite se:
			<input TYPE=\"text\" NAME=\"user\" value=\"\">
			<input TYPE=\"password\" NAME=\"pass\" value=\"\">
			<input type=\"submit\" value=\"Prijavi se\" /><h4>
		</form>
		<h3>Ili ako niste registrirani korisnik možete se <a href=\"registracija.php\">REGISTRIRATI</a></h3><br/>";
	else if ($klijent != 0 )
		echo "<a href=\"logout.php\">Odjava</a><br/>";
	?>
	<h2><font size="5" face="arial" color="red">Dobro dosli na stranicu Web shoPP !!!</font></h2>
	<?php
	
	if ($klijent === -2) {
		echo "<h3>Želite registrirati nove korisnike? <a href=\"registracija.php\">Registriraj</a><br/><br/><a href=\"prikaziKlijente.php\">Pregledaj klijente</a></h3>";
		
	}
	if ($klijent > 0) 
		echo "<a href=\"prikaziKosaricu.php\">Pogledaj stanje kosarice</a><br/>";
	
	if ($klijent < 0) {
		echo "<a href=\"obradiKosarice.php\">Obradi kosarice</a><br/><br/>";
		echo "<a href=\"dodajArtikl.php\">Dodaj artikl</a><br/>";
	}
	
	
	?>
	<h3>Možete pregledavati artikle
	<a href="prikaziArtikle.php">PREGLEDAJ</a></h3><br/>
	<h3>.. ili ih pretraživati:</h3>	
	<form name="pretrazi" action="prikaziArtikle.php" method="get">
		<h4>Pretrazuje se po imenu:
		<input TYPE="text" NAME="searchBy" value="">
		<input type="submit" value="Pretraži" /><h4>
	</form>
</body>
<html>
<?php
/*

CREATE  TABLE IF NOT EXISTS `dz5_korisnici` (
  `id` INT NOT NULL  AUTO_INCREMENT ,
  `username` VARCHAR(45) NOT NULL ,
  `lozinka` VARCHAR(45) NOT NULL ,
  `ime` VARCHAR(45) NOT NULL ,
  `prezime` VARCHAR(45) NOT NULL ,
  `adresa` VARCHAR(45) NOT NULL ,
  `zakljucanaKosarica` BOOLEAN NOT NULL,
  `privilegije` VARCHAR(45) NOT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;

CREATE UNIQUE INDEX `userUNIQ` ON `dz5_korisnici` (`username`) ;
CREATE UNIQUE INDEX `personUNIQ` ON `dz5_korisnici` (`ime`, `prezime`, `adresa`) ;

CREATE  TABLE IF NOT EXISTS `dz5_artikli` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `naziv` VARCHAR(45) NOT NULL ,
  `opis` VARCHAR(255) NOT NULL ,
  `cijena` DOUBLE NOT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;

CREATE UNIQUE INDEX `artUNIQ` ON `dz5_artikli` (`naziv`) ;

CREATE  TABLE IF NOT EXISTS `dz5_artikliSlike` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `idArtikla` INT NOT NULL ,
  `mimeType` VARCHAR(45) NOT NULL ,
  `data` mediumblob NOT NULL ,
  `size` INT NOT NULL,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `postojanjeArtikla`
    FOREIGN KEY (`idArtikla` )
    REFERENCES `dz5_artikli` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;

CREATE  TABLE IF NOT EXISTS `dz5_kosarica` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `idArtikla` INT NOT NULL ,
  `idKorisnika` INT NOT NULL ,
  `komada` INT NOT NULL ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `postojanjeArtiklaKosarice`
    FOREIGN KEY (`idArtikla` )
    REFERENCES `dz5_artikli` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `postojanjeKupcaKosarice`
    FOREIGN KEY (`idKorisnika` )
    REFERENCES `dz5_korisnici` (`id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;

CREATE UNIQUE INDEX `cartUNIQ` ON `dz5_kosarica` (`idArtikla`, `idKorisnika`) ;

*/
?>