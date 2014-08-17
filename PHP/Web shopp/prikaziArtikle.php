<?php
require_once 'lib/dbControler.php';
require_once 'lib/pager.php';

session_start();

$klijent = null;
$idOfBuyer = null;

$lastMinuteAddOn = null; // isa sam dodat evo sad na kraju da se vidi kosarica odma kad kupuje

if (!isset($_SESSION['CLIENT'])) { 
	$klijent = new Guest();
	unset($_SESSION);
	session_destroy();
	$lastMinuteAddOn = false;	
}
else if ($_SESSION['CLIENT'] > 0 ) {
	$idOfBuyer = $_SESSION['CLIENT'];
	$klijent = new User($idOfBuyer);
	$lastMinuteAddOn = true;
}
else {
	$klijent = new Trader();
	$lastMinuteAddOn = false;
}

$deleted = null;
if(isset($_GET['delete']) && $_SESSION['CLIENT'] < 0) {
		if (!isset($_SESSION['SWARM']))
			$_SESSION['SWARM'] = array();
		else if (in_array($_GET['delete'], $_SESSION['SWARM']))
			$deleted = false;
		
		if ($deleted === null) {
			$klijent->izbrisiArtikl($_GET['delete']);
			$deleted = true;
			$_SESSION['SWARM'][] = $_GET['delete'];
		}
}

function escape(&$arr) { // last minute dodano :D
	$rez = '';
	for($i = 0; $i < strlen($arr); ++$i) 
		if ($arr[$i] === '%' || $arr[$i] === '\'' || $arr[$i] === '_' || $arr[$i] === '\\') {
			$rez .= '\\';
			$rez .= $arr[$i];
		}
		else
			$rez .= $arr[$i];

	return $rez;
}	

$pager = null;
$page = 1;
$searchBy = null;

if (!isset($_GET["searchBy"]))
	$pager = new Pager($klijent->skupiArtikle());
else if ($_GET["searchBy"] == '')
	$pager = null;
else {
	$searchBy = escape($_GET["searchBy"]);
	$pager = new Pager($klijent->skupiArtikle($searchBy));
}

if (isset($_GET["page"])) {
	if ($_GET["page"] > 0 && $_GET["page"] <= $pager->getTotalPages()) {
		$page = $_GET["page"];
		$pager->setPage($page);
	}
	else if ($deleted === false && ($_GET["page"] > 1 && ($_GET["page"] - 1) <= $pager->getTotalPages())) {
		$page = $_GET["page"] - 1; // da se refreshalo bila bi jedna stranica manje;
		$pager->setPage($page);
		echo "NEEE";
	}
	else
		$pager = null;
}

function keepItReal() {
	if (!isset($_GET["searchBy"]))
		return '';
	else 
		return "&searchBy=" . $_GET["searchBy"];
}
 
$bought = null;
if(!empty($_POST) && $idOfBuyer != null) {
	$artikli = array_keys($_POST); // zasada radi samo s jednim, ali tu je za prosirenja
	foreach($artikli as $artikl)
		if (is_numeric($artikl) && (is_numeric($_POST[$artikl]) || $_POST[$artikl] === ''))
			if ($_POST[$artikl] === '') 
				$bought = $klijent->addToCart($artikl);
			else 
				$bought = $klijent->addToCart($artikl, $_POST[$artikl]);
		else
			$bought = false;
}
?>
<html>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<head><title> Showing articles </title></head>
<body>
	<?php
	if ($deleted === true)
		echo "Artikl je uspjesno obrisan.<br/>";
	else if ($deleted === false)
		echo "(Kliknuli ste na artikl koji je već bio izbrisan, ali stranica nije bila refreshed!)<br/>";
	else if (isset($_GET['delete']))
		echo "Vaš pokušaj prevare je zabilježen. Biti ćete s vremenom kažnjeni. Nastavite s radom.<br/>";
	
	if (!($bought === null)) 
		if ($bought)
			echo "Vaša narudžba je zabilježena<br/>";
		else 
			echo "Vaš pokušaj prevare je zabilježen. Biti ćete s vremenom kažnjeni. Nastavite s radom.<br/>zapravo: Ili vam je košarica zaključana ili ste na popisu zločeste djece.";
	
	if($pager == null && $deleted == true)
		echo "Nema više artikala.<br/>";
	else if ($pager == null)
		echo "Vaš pokušaj prevare je zabilježen. Biti ćete s vremenom kažnjeni. Nastavite s radom.";
	else {
		$pager->showPage($page, $searchBy);
	
	
	if ($pager->getCurrentPage() > 1)
		echo "<a href=\"prikaziArtikle.php?page=" . ($pager->getCurrentPage() - 1) . keepItReal() . "\">Lijevo</a><br/>";
	if ($pager->getCurrentPage() < $pager->getTotalPages())
		echo "<a href=\"prikaziArtikle.php?page=" . ($pager->getCurrentPage() + 1) . keepItReal() . "\">Desno</a><br/>";
	
	$from = $pager->getCurrentPage();
	$to = $pager->getTotalPages();
	if ($to > ($from + 10))
		$to = $from + 10;
	
	if ($from > 1)
		print "<a href=\"prikaziArtikle.php?page=1" . keepItReal() . "\">1</a> ..&nbsp;";
	
	for(; $from <= $to; ++$from)
		print "<a href=\"prikaziArtikle.php?page=" . $from . keepItReal() . "\">" . $from . "</a>&nbsp;";
	
	if ($to < $pager->getTotalPages())
		print "<a href=\"prikaziArtikle.php?page=" . $pager->getTotalPages() . keepItReal() . "\">" .  $pager->getTotalPages() . "</a>&nbsp;";
	}
	
	
	if ($lastMinuteAddOn == true) {
		echo "<br/><br/>Trenutno stanje vaše košarice:<br/><br/>";
		if($klijent->haveIOrdered() )
			echo "Vaša prošla narudžba se još obrađuje. Kada primite svoju robu i platite račun. Srdačno vas molimo da nas posjetite opet<br/>";
		else 
			$klijent->prikaziKosaricu();
	}
	?>
	<br/><br/><a href="index.php">Back to life. Back to reality.</a><br/>
</body>
<html>