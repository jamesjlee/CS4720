<?php
	$count = $_GET['count'];
	$cardMax = 360;
	$difference = $cardMax - $count;
	if ($difference != 0) {
		echo "You added $count card(s).<br>";
		echo "($count / $cardMax)<br>";
		echo "$difference more cards can be added.";
	}
	else {
		echo "You cannot add any more cards. You've reached the maximum amount of cards in the cube - $cardMax.";
	}
?>