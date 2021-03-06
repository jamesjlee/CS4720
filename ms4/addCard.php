<?php
function addCard($cardName) {
	$db_connection = new mysqli('stardock.cs.virginia.edu', 'cs4720jjl9ve', 'fall2014', 'cs4720jjl9ve');
	if (mysqli_connect_errno()) {
			echo "Connection Error!";
			return;
	}

	$stmt = $db_connection->stmt_init();
	$stmt2 = $db_connection->stmt_init();

	if($stmt->prepare("INSERT INTO cube (name, type, cost, comments) SELECT * FROM allCards WHERE name = ?")) {
		$stmt->bind_param("s", $cardName);
		$stmt->execute();
		if ($stmt2->prepare("SELECT * FROM cube")) {
		$stmt2->execute();
		$stmt2->bind_result($name, $type, $cost, $comments);
		echo "<table>";
		echo "<tr><th>Name</th><th>Type</th><th>Cost</th><th>Comments</th>";
		while($stmt2->fetch()) {
			echo "<tr>";
			echo("<td>" . "<a href='http://plato.cs.virginia.edu/~jjl9ve/ms4/img/cube_imgs/" 
				. $name . ".jpg'>" . $name . "<div id=cardImage><img src='http://plato.cs.virginia.edu/~jjl9ve/ms4/img/cube_imgs/" 
				. $name . ".jpg' style=height:50px; width:50px>" . "</div></a></td>\n");
			echo("<td>" . $type . "</td>\n");
			echo("<td>" . "<div id=cost>$cost</div></td>\n");
			echo("<td>" . $comments . "</td>\n");
			echo "</tr>";
		}
		echo "</table>";
		}
	}
}
	$cardName = $_GET["trimmedCardName"];
	addCard($cardName);
?>