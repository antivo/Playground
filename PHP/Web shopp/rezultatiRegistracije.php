<?
require_once 'lib/dbControler.php';

session_name(sha1('si44835'));
session_start();

function isItCompleteForm() {
	foreach($_POST as $entry)
		if($entry === '')
			return false;
	return true;
}

function navediRazlog(&$status) {
	switch ($status) {
		case -1: return 'Korisnik s tim korisničkim imenom već postoji!';
		case -2: return 'Osoba s tim podatcima već postoji. Ne mogu postojati dvije iste osobe, ako se pita bazu podataka ! ';
		case -3: return 'Vaš pokušaj prevare je zabilježen. Biti ćete s vremenom kažnjeni. Nastavite s radom. ';
	}
}

function escape(&$arr) { // last minute dodano :D
	$rez = '';
	for($i = 0; $i < strlen($arr); ++$i) 
		if ($arr[$i] === '%' || $arr[$i] === '\'' || $arr[$i] === '_' || $arr[$i] === '\\') {
			$rez .= '\\';
			$rez .= $arr[$i];
		}
		else
			$rez .= $arr[$i];

	return $rez;
}	
?>
<html>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<head><title> LOGEDIN </title></head>
<body>
	<?php
	if (empty($_POST)) 
		echo "Vaš pokušaj prevare je zabilježen. Biti ćete s vremenom kažnjeni. Nastavite s radom. <br/> zapravo: PAGE EXPIRED";
	else if (!isItCompleteForm())
		echo "Vaš pokušaj prevare je zabilježen. Biti ćete s vremenom kažnjeni. Nastavite s radom. <br/> zapravo: NISTE UNJELI SVE PODATKE";
	else if ($_POST['lozinka'] != $_POST['lozinka2'])
		echo "Vaš pokušaj prevare je zabilježen. Biti ćete s vremenom kažnjeni. Nastavite s radom. <br/> zapravo: UNJELI STE DVIJE RAZLIČITE LOZINKE";
	else if(isset($_POST['privilegije'])) {
		if (!($_POST['privilegije'] === 'korisnik' || $_POST['privilegije'] === 'tgovac' || $_POST['privilegije'] === 'admin')) 
			echo "Vaš pokušaj prevare je zabilježen. Biti ćete s vremenom kažnjeni. Nastavite s radom. <br/> zapravo: \"". $_POST['privilegije'] . "\"  NIJE PONUĐENA PRIVILEGIJA.";
	}
	else if (strlen(htmlspecialchars($_POST['username'])) > 45)
		if	(strlen(htmlspecialchars($_POST['username'])) === strlen($_POST['username']))
			echo "Vaš pokušaj prevare je zabilježen. Biti ćete s vremenom kažnjeni. Nastavite s radom. <br/> zapravo: \"". htmlspecialchars($_POST['username']) . "\"  JE PREDUGACKO KORISNICKO IME (do 45 znakova).";
		else
			echo "Vaš pokušaj prevare je zabilježen. Biti ćete s vremenom kažnjeni. Nastavite s radom. <br/> zapravo: \"". htmlspecialchars($_POST['username']) . "\"  JE PREDUGACKO KORISNICKO IME (do 45 znakova) (koristite manje html tagova ;)).";
		else {
		$username    = htmlspecialchars($_POST['username']);
		$lozinka     = sha1($_POST['lozinka']);
		$ime         = htmlspecialchars($_POST['ime']);
		$prezime     = htmlspecialchars($_POST['prezime']);
		$adresa      = htmlspecialchars($_POST['adresa']);
		
		$registered = false;
		$reason = null;

		$zapisnicar = new Zapisnicar();
		if (isset($_SESSION['CLIENT'])) 
			
			if ($_SESSION['CLIENT'] === -2) {
				$privilegije = htmlspecialchars($_POST['privilegije']);
				$status = $zapisnicar->registerUser($username, $lozinka, $ime, $prezime, $adresa, $privilegije);
			}
			else {
				echo "Vaš pokušaj prevare je zabilježen. Biti ćete s vremenom kažnjeni. Nastavite s radom. <br/>";
				die();
			}
		else
		$status = $zapisnicar->registerUser($username, $lozinka, $ime, $prezime, $adresa);
		
		if ($status === 0)
			echo "Račun s korisničkim imenom: " . $username . " je uspjesno registriran.";
		else 
			echo "Niste se uspjeli registrirati<br/>Razlog: " . navediRazlog($status) .  "<br/><br/><a href=\"registracija.php\">Back to registracija. Everyday I'm shuffling.</a><br/>";
	}
	?>
	<br/><br/><a href="index.php">Back to life. Back to reality.</a><br/>
</body>
<html>