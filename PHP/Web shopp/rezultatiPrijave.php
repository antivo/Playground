<?php
require_once 'lib/dbControler.php';

session_start();

function escape(&$arr) { // last minute dodano :D
	$rez = '';
	for($i = 0; $i < strlen($arr); ++$i) 
		if ($arr[$i] === '\'' || $arr[$i] === '\\') {
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
<head><title> Rezultati prijave </title></head>
<body>
	<?
	if(isset($_SESSION) && !empty($_SESSION)) {
		if (!empty($_POST)) 
			echo "Vaš pokušaj prevare je zabilježen. Biti ćete s vremenom kažnjeni. Nastavite s radom. <br/>";
		else 
			echo "Ovdje nemate nekog posla... sve vam piše ispod.<br/>";
	}
	else {
		if (empty($_POST)) {
			echo "Vaš pokušaj prevare je zabilježen. Biti ćete s vremenom kažnjeni. Nastavite s radom. <br/> zapravo: PAGE EXPIRED";
			$_SESSION = array();
			session_destroy();
		}
		else {
			$user = escape(htmlspecialchars($_POST['user']));
			$pass = sha1($_POST['pass']);
			//var_dump($user);
			//var_dump($pass);
			
			$evoker = new Evoker();
			$indentifier = $evoker->invUlogiraj($user, $pass);
			
			if ($indentifier === 0) {
				echo "Pogrešan unos korisničkog imena i lozinke ! Vratite se i pokušajte ponovo. Možete isprobati zilion puta. ";
				$_SESSION = array();
				session_destroy();
			}
			else {
				echo 'Ulogirali ste se';
				
				$_SESSION['CLIENT'] = $indentifier;
			}
		}
	}
	?>
	<br/><br/><h4>Molim kliknite na link ispod kako bi vas stranica preusmjerila jer sam ja lijen napraviti to sam<h4>
	<a href="index.php">Back to life. Back to reality.</a><br/>
</body>
</html>