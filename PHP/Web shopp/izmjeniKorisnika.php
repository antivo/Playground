<?php
require_once 'lib/dbControler.php';

session_start();

if(!isset($_SESSION['CLIENT']))
	die();
else if ($_SESSION['CLIENT'] > -2)
	die();
	
if(!isset($_GET['id']))
	die();

$id = $_GET['id'];

$zapisnicar = new Zapisnicar();

if(!$zapisnicar->doesItExist($id))
	die();
	
if (!empty($_POST)) {
	$ime = $_POST['ime'];
	$prezime = $_POST['prezime'];
	$adresa = $_POST['adresa'];
	$zapisnicar->izmjeniKlijenta($id, $ime, $prezime, $adresa);
}
	
?>
<html>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<head><title> Načini izmjene nad korisnikom </title></head>
<body>
	<?php
		$zapisnicar->prikaziKlijenta($id);
	?>
	<h4>Ispunite samo polja koja želite da budu izmjenjena</h4>
	<br/><form name="registracija" action="izmjeniKorisnika.php<?php echo "?id=" . $id; ?>" method="post">
		Novo ime:<input TYPE="text" NAME="ime" value=""><br/><br/>
		Novo prezime:<input TYPE="text" NAME="prezime" value=""><br/><br/>
		Nova adresa:<input TYPE="text" NAME="adresa" value=""><br/><br/>
		<input type="submit" value="Predaj" />
        <input type="reset" value="Reset" />
	</form><br/><br/>
	<a href="index.php">Back to life. Back to reality.</a><br/>
</body>
</html>