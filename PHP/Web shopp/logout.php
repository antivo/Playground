<?php
session_start();

if(!isset($_SESSION['CLIENT'])) {
	unset($_SESSION);
	session_destroy();
	header( 'Location: http://www.google.com' ) ; // umjesto die() nudim troll rjesenje
}

if(isset($_SESSION)) {
	unset($_SESSION);
	session_destroy(); 
}
?>
<html>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<head><title> Log out </title></head>
<body>
	<h3> Odjavili ste se. Bon voyage. </h3><br/>
	<a href="index.php">Back to life. Back to reality.</a><br/>
</body>
</html>