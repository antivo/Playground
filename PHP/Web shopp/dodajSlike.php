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
<head><title> Upload </title></head>
<body>
	<?php
	$check = new Trader();
	
	$id = null; 
	
	if (!isset($_GET['idArtikla'])) {
		echo "Vaš pokušaj prevare .... bla bla .. uglavnom .. ne varajte.";
		die();
	}
	else {
		$id = $_GET['idArtikla'];
		if (!$check->doesItExist($id)) {
			echo "Vaš pokušaj prevare .... bla bla.. uglavnom .. ne varajte.";
			die();
		}
	}
	?>
	<form action="rezAddPic.php" method="POST" enctype="multipart/form-data">
             <fieldset>
                 <legend>Upload</legend>
                 <p>
                    <label for="datoteka">Datoteka: </label>
                     <input name="image" accept="image/jpeg" type="file">
					 <?php
					 echo "<input type=\"hidden\" name=\"idArtikla\" value=\"" . $id . "\">";
					 ?>
				 </p>
                 <p>
                     <input type="submit" name="upload" value="Upload">
                 </p>
             </fieldset>
         </form>
	<a href="index.php">Back to life. Back to reality.</a><br/>
</body>
</html>