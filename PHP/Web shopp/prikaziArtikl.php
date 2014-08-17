<?php
require_once 'lib/dbControler.php';

if(!isset($_GET['idArtikl'])) {
	echo "vaš pokušaj prevare me umara.";
	die();
}
$id = $_GET['idArtikl'];

$trader = new Trader();

if(!$trader->doesItExist($id)) {
	echo "vaš pokušaj ...  prevare ... la la la la";
	die;
}
?>
<html>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<head><title> <?=$trader->getName($id); ?> </title></head>
<body>
	<?php
		$trader->tryToSell($id);
	?>
	<br/><br/><a href="index.php">Back to life. Back to reality.</a><br/>
</body>
</html>