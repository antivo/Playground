<?php
session_start();

$adminIsHiere = null;

if(!isset($_SESSION['CLIENT'])) { // JUST A GUEST
	unset($_SESSION);
	session_destroy();
}
else if ($_SESSION['CLIENT'] > -2)
	$adminIsHiere = false;
else
	$adminIsHiere = true;

if ($adminIsHiere === false)
	die();

?>
<html>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<head><title> Registracija novog korisnika </title></head>
<body>
	<form name="registracija" action="rezultatiRegistracije.php" method="post">
		Željeno korisničko ime:<input TYPE="text" NAME="username" value=""><br/><br/>
		Željena loznka :<input type="password" name="lozinka" size="20"><br/><br/>
		Željena loznka :<input type="password" name="lozinka2" size="20"><br/><br/>
		Vaše ime:<input TYPE="text" NAME="ime" value=""><br/><br/>
		Vaše prezime:<input TYPE="text" NAME="prezime" value=""><br/><br/>
		Vaša adresa:<input TYPE="text" NAME="adresa" value=""><br/><br/>
		<?php
		if ($adminIsHiere === true)
			echo "Privilegije (korisnik, trgovac, admin):<input TYPE=\"text\" NAME=\"privilegije\" value=\"\"><br/><br/>";
		?>
		<input type="submit" value="Predaj" />
        <input type="reset" value="Reset" />
	</form>
	<br/><br/><a href="index.php">Back to life. Back to reality.</a><br/>
</body>
<html>