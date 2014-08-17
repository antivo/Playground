<?
require_once 'lib/dbControler.php';

session_start();

if(!isset($_SESSION['CLIENT']))
	die();
else if ($_SESSION['CLIENT'] > 0)
	die();

function isItCompleteForm() {
	foreach($_POST as $entry)
		if($entry === '')
			return false;
	return true;
}

function navediRazlog(&$status) {
	switch ($status) {
		case -1: return 'Artikl s tim nazivom već postoji!';
	}
}

?>
<html>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<head><title> Rezultati akcije </title></head>
<body>
	<?php
	if (empty($_POST)) 
		echo "Vaš pokušaj prevare je zabilježen. Biti ćete s vremenom kažnjeni. Nastavite s radom. <br/> zapravo: PAGE EXPIRED";
	else if (!isItCompleteForm())
		echo "Vaš pokušaj prevare je zabilježen. Biti ćete s vremenom kažnjeni. Nastavite s radom. <br/> zapravo: NISTE UNJELI SVE PODATKE";
	else if (!is_numeric($_POST['cijena']))
		echo "Vaš pokušaj prevare je zabilježen. Biti ćete s vremenom kažnjeni. Nastavite s radom. <br/> zapravo: CIJENA ARTIKLA MORA BITI BROJ";
	else {
		$naziv  = htmlspecialchars($_POST['naziv']);
		$opis   = htmlspecialchars($_POST['opis']);
		$cijena = $_POST['cijena'];
		
		$registered = false;
		$reason = null;
		
		$zapisnicarTrgovac = new Trader();
		$status = $zapisnicarTrgovac->dodajArtikl($naziv, $opis, $cijena);
		
		if ($status === 0)
			echo "Artikl s nazivom imenom: " . $naziv . " je uspjesno dodan.";
		else 
			echo "Niste uspjeli dodati artikl<br/>Razlog: " . navediRazlog($status) ;
	
	}
	?>
	<br/><br/><br/><a href="dodajArtikl.php">Želite ponovo pokušati dodati artikl.. GET ON THE SQUIRREL, NO TIME TO EXPLAIN!</a>"
	<br/><br/><br/><a href="index.php">Back to life. Back to reality.</a><br/>
</body>
</html>