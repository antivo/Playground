<?php
require_once 'lib/dbControler.php';

class Pager {
	private $lastIndex;
	private $totalPages;
	private $startOfCurrent;
	private $endOfCurrent;
	private $currentPage;
	private $context;
	
	public function __construct($lines) {
		$this->context = $lines;
		$this->lastIndex = sizeof($lines);
		$this->totalPages = ceil($this->lastIndex / 5);
		$this->lastIndex -= 1; 

		$this->currentPage = 1;
		$this->startOfCurrent = 0;
		$this->setEnd();
	}
	
	public function getCurrentPage() {
		return $this->currentPage;
	}
	
	public function getTotalPages() {
		return $this->totalPages;
	}
	
	private function setStart() {
			$this->startOfCurrent = ($this->currentPage - 1) * 5;
	}
	
	private function setEnd() {
		if ($this->totalPages > $this->currentPage)
			$this->endOfCurrent = $this->currentPage * 5 - 1;
		else
			$this->endOfCurrent = $this->lastIndex;
	}
	
	public function setPage($page) {
		$this->currentPage = $page;
		
		$this->setStart();
		$this->setEnd();
	}
	
	public function nextPage() {
		$this->startOfCurrent = $this->endOfCurrent + 1;
		$this->currentPage += 1;
		$this->setEnd();
	}
	
	public function previousPage() {
		$this->setStart();
		$this->setEnd();
	}
	
	protected function keepItReal(&$page, &$searchBy) {
		$rage = '';
		if ($page === null)
			return '';
		else {
			$rage = '?page=' . $page;
			if ($searchBy === null)
				return $rage;
			else
				return $rage = $rage . '&searchBy=' . $searchBy;
		}
		return $rage;
	}
	
	protected function hyperUredi(&$id, $page, &$searchBy, $guard) {
		if ($page != null || $searchBy != null) {
			if ($guard === 1 && $page != 1) // ono kad je na prijelomu stranice
				--$page;
			return "<a href=\"dodajSlike.php?idArtikla=" . $id . "\">Dodaj_slikE</a>&nbsp;&nbsp;<a href=\"izmjeniArtikl.php?update=" . $id . "\">UPDATE</a>&nbsp;&nbsp;<a href=\"prikaziArtikle.php" . $this->keepItReal($page, $searchBy) . "&delete=" . $id . "\">DELETE</a>";
		}
		else
			return "<a href=\"dodajSlike.php?idArtikla=" . $id . "\">Dodaj_slikE</a>&nbsp;&nbsp;<a href=\"izmjeniArtikl.php?update=" . $id . "\">UPDATE</a>&nbsp;&nbsp;<a href=\"prikaziArtikle.php?delete=" . $id . "\">DELETE</a>";
	}
	
	protected function formAddToCart(&$idArtikla, &$page, &$searchBy) {
		return "<form name=\"formAddToCart\" action=\"prikaziArtikle.php" . $this->keepItReal($page, $searchBy) . "\" method=\"post\">
			<h4>how much?:
			<input TYPE=\"text\" NAME=\"". $idArtikla ."\" value=\"\">
			<input type=\"submit\" value=\"Add to Cart\" /><h4>
		</form>";
	}
	
	public function showPage(&$page = null, &$searchBy = null) {
		if ($this->startOfCurrent === ($this->endOfCurrent + 1)) {
			echo "Nažalost, vaša pretraga nije dala nikakav rezultat.";
			return;
		}

		echo "<br/><table border=\"1\">";
		$guard=0;
		for($i = $this->startOfCurrent; $i <= $this->endOfCurrent; ++$i){ 
			echo "<tr>";
			++$guard;
			$j=0;
			$idArtikla = -1;
			
			$painter = new Painter();
			
			foreach ($this->context[$i] as $cell) {
				if ($j === 4) {
					echo  "<td><a href=\"prikaziArtikl.php?idArtikl=" . $cell . "\">Detaljnije</a></td>";
					$idArtikla = $cell;
				}
				else if ($j === 2)
					echo  "<td>" . $cell . " kn</td>";
				else if ($cell === 'Add to Cart')
					echo "<td>" . $this->formAddToCart($idArtikla, $page, $searchBy) . "</td>";
				else if ($cell === 'Uredi')
					echo "<td>" .  $this->hyperUredi($idArtikla, $page, $searchBy, $guard%5) . "</td>";
				else if ($cell === null && $j === 3)
					echo  "<td>nema sliku</td>";
				else if ($j === 3) 
					echo "<td>" . $painter->showPicture($cell) ."</td>";
				else
					echo  "<td>" . $cell . "</td>";
				++$j;
				}
			echo "</tr>";
		}
		
		echo "<table><br/>";
	}
}
?>