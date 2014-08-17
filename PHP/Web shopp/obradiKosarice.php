<?php
require_once 'lib/dbControler.php';

session_start();

if(!isset($_SESSION['CLIENT']))
	die();
else if ($_SESSION['CLIENT'] > 0)
	die();


$korisnik = new Trader();

?>
<html>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<head><title> Obradi kosarice </title></head>
<body>
	<?php
		if(isset($_GET['obradi'])) {
			$id = $_GET['obradi'];
			$korisnik->procesuirajKosaricu($id);
			echo "Uspjesno ste obradili kosaricu.<br/><br/>";
		}
	
		$korisnik->pregledajKosarice();
	?>
	
	<a href="index.php">Back to life. Back to reality.</a><br/>
</body>
</html>