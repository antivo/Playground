<?php
require_once 'lib/dbControler.php';

session_start();

if(!isset($_SESSION['CLIENT']))
	die();
else if ($_SESSION['CLIENT'] > -2)
	die();

$zapisnicar = new Zapisnicar();

$deleted = null;
if(isset($_GET['delete'])) {
		if (!isset($_SESSION['SWARM']))
			$_SESSION['SWARM'] = array();
		else if (in_array($_GET['delete'], $_SESSION['SWARM']))
			$deleted = false;
		
		if ($deleted === null) {
			$zapisnicar->izbrisiKlijenta($_GET['delete']);
			$deleted = true;
			$_SESSION['SWARM'][] = $_GET['delete'];
		}
}
?>
<html>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<head><title>Pregled Klijenata</title></head>
<body>
	<?php
		if ($deleted === true)
			echo "Klijent je uspjesno obrisan.<br/>";
		else if ($deleted === false)
			echo "(Kliknuli ste na klijent koji je već bio izbrisan, ali stranica nije bila refreshed!)<br/>";
		else if (isset($_GET['delete']))
			echo "Vaš pokušaj prevare je zabilježen. Biti ćete s vremenom kažnjeni. Nastavite s radom.<br/>";
	
		$zapisnicar = new Zapisnicar();
		$zapisnicar->pregledajKorisnike();
		
	?>
	<a href="index.php">Back to life. Back to reality.</a><br/>
</body>
</html>