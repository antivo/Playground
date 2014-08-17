<?php
require_once 'lib/dbControler.php';

session_start();

if(!isset($_SESSION['CLIENT']))
	die();
else if ($_SESSION['CLIENT'] > 0)
	die();
?>
<html>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<head><title> Dodaj artikl </title></head>
<body>
	<h3> Ako zelite dodati novi artikl, molim ispunite podatke dolje. </h3><br/>
	<form name="registracija" action="rezAddArt.php" method="post">
		Ime artikla:<input TYPE="text" NAME="naziv" value=""><br/><br/>
		Opis artikla:<br/><textarea NAME="opis" cols=35 rows=7></textarea><br/><br/>
		Cijena artikla:<input TYPE="text" NAME="cijena" value=""><br/><br/>
		<input type="submit" value="Predaj" />
        <input type="reset" value="Reset" />
	</form>
	
	<a href="index.php">Back to life. Back to reality.</a><br/>
</body>
</html>