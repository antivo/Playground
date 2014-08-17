<?php
require_once 'lib/dbControler.php';

session_start();

if(!isset($_SESSION['CLIENT']))
	die();
else if ($_SESSION['CLIENT'] < 1)
	die();
	
$korisnik = new User($_SESSION['CLIENT']);

$odluka = null;
if (isset($_GET['odluka']))
	if ($_GET['odluka'] == 'odbaci')
		$odluka = false;
	else if ($_GET['odluka'] == 'kupi')
		$odluka = true;

?>
<html>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<head><title> Prikazi kosaricu </title></head>
<body>
	<h5>Ovo su artikli koji se nalaze u vašoj košarici.<br/></h5>
	<?php
	if($korisnik->haveIOrdered())
		echo "Vaša prošla narudžba se još obrađuje. Kada primite svoju robu i platite račun. Srdačno vas molimo da nas posjetite opet<br/>";
	else if ($odluka === null)
		$korisnik->prikaziKosaricu();
	else 
		if ($odluka) {
			$korisnik->order();
			echo "Naručili ste košaricu. Molimo sačekajte dok jedan od naših trgovaca ju ne obradi.<br/>";
		}
		else {
			$korisnik->reject();
			echo "Vaša košarica je ispražnjena<br/>";
		}
	?>
	<br/><a href="index.php">Back to life. Back to reality.</a><br/>
</body>
</html>