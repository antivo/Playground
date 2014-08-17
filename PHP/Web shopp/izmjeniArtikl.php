<?php
require_once 'lib/dbControler.php';

session_start();

if(!isset($_SESSION['CLIENT']))
	die();
else if ($_SESSION['CLIENT'] > 0)
	die();
	
if(!isset($_GET['update']))
	die();	

$id = $_GET['update'];

$trader = new Trader();

if(!$trader->doesItExist($id))
	die();
	

if(isset($_GET['delete'])) 
	$trader->obrisiSliku($id, $_GET['delete']); // $id da ne moram radit provjeru postoji li ta slika s tim kljucem


$dang = null;
if (!empty($_POST)) {
	$cijena = $_POST['cijena'];
	if($cijena !='' && !is_numeric($cijena)) 
		$dang = false;
	else if ($cijena < 0)
		$dang = false;
	else{
		$dang = true;
		$naziv = $_POST['naziv'];
		$opis = $_POST['opis'];
		$trader->izmjeniArtikl($id, $naziv, $opis, $cijena);
	}
}
?>
<html>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<head><title> Dodaj artikl </title></head>
<body>
	<?php
		if ($dang === false) 
			echo "You've been a bad, bad girl. Vaša izmjena se zanemaruje zapravo: cijena mora biti broj veći ili jednak nula (tako je imamo i besplatne proizvode ).<br/><br/>";
			
		$trader->ispisiOsnovnePodatke($id);
	?>
	<h4>Ispunite samo ona polja gdje želite načiniti izmjene</h4><br/><br/>
	<form name="registracija" action="izmjeniArtikl.php<?php echo "?update=" . $id; ?>" method="post">
		Ime artikla:<input TYPE="text" NAME="naziv" value=""><br/><br/>
		Opis artikla:<br/><textarea NAME="opis" cols=35 rows=7></textarea><br/><br/>
		Cijena artikla:<input TYPE="text" NAME="cijena" value=""><br/><br/>
		<input type="submit" value="Predaj" />
        <input type="reset" value="Reset" />
	</form>
	<?php
		$trader->ponudiSlike($id);
	?>
	<a href="index.php">Back to life. Back to reality.</a>
</body>
</html>