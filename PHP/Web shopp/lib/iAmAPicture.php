<?php
require_once 'lib/dbControler.php';

if(isset($_GET['id'])) 
	if ($_GET['id']!='' && is_numeric($_GET['id'])) { 
	$painter = new Painter();

	$id = $_GET['id'];
	$file = $painter->getPicture($id);

	header('Content-type: image/jpg');
	header('Content-length: ' .  $file['size']);

	echo $file['data'];
	}
	else {
		echo "You're such a bad bad girl.";
		die();
	}
else { // nikada se nece dogodit .. 
	echo "NO.";
	die();
}
?>