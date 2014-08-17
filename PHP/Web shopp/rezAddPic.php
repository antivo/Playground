<?php
require_once 'lib/dbControler.php';

session_start();

if(!isset($_SESSION['CLIENT']))
	die();
else if ($_SESSION['CLIENT'] > 0)
	die();
	
function isFake() {
	if(isset($_POST['idArtikla']))
		return false;
	else
		return true;
}
?>
<html>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<head><title> Rezultat akcije </title></head>
<body>
	<?php
	if (isFake()) {
		echo "Opet ste pokusali varati. ZLOCO JEDNA";
		die();
	}
	
	$id = $_POST['idArtikla'];
	
	if((stristr($_FILES['image']['type'], "image") === FALSE)) {
		echo "Vaš upload se zanemaruje. Ono što ste htjeli uploadati nije slika.<br/>";
	}
	else if (isset($_FILES['image']) && $_FILES['image']['size'] > 0) { 
		$tmpName  = $_FILES['image']['tmp_name'];  
      
		$fp   = fopen($tmpName, 'r');
		$data = fread($fp, filesize($tmpName));
		
		fclose($fp);
     
		$korisnik = new Trader();
		$status = $korisnik->dodajSliku($id, $_FILES['image']['type'], $data, $_FILES['image']['size']);
	  
		if ($status < 0)
			echo "Neocekivana greška, molim pokušajte ponovo.<br/>";
		else
			echo "Vaša slika je uspješno priložena.<br/>";
      
	}
	else {
		print "Vaš pokušaj prev ... meh. No image selected/uploaded<br/>";
	}

	echo "<br/><br/><br/><a href=\"dodajSlike.php?idArtikla=" . $id . "\">Dodati novu sliku ovome artiklu ? .. HOP ON</a>";
	?>
	<br/><br/><br/><a href="index.php">Back to life. Back to reality.</a><br/>
</body>
</html>