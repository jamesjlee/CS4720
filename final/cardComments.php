<?php
function comments($comments, $cardName) {
	$db_connection = new mysqli('stardock.cs.virginia.edu', 'cs4720jjl9ve', 'fall2014', 'cs4720jjl9ve');
	if (mysqli_connect_errno()) {
			echo "Connection Error!";
			return;
	}

	$stmt = $db_connection->stmt_init();
	$stmt1 = $db_connection->stmt_init();
	$stmt2 = $db_connection->stmt_init();

	if ($stmt->prepare("UPDATE cube SET comments = ? WHERE name = ?")) {
		$stmt->bind_param("ss", $comments, $cardName);
		$stmt->execute();
		if($stmt1->prepare("UPDATE allCards SET comments = ? WHERE name = ?")) {
			$stmt1->bind_param("ss", $comments, $cardName);
			$stmt1->execute();
		}
		if ($stmt2->prepare("SELECT * FROM cube")) {
			$stmt2->execute();
			$stmt2->bind_result($name, $type, $cost, $comments);
			echo "<table id='cubeTable' class='table table-hover'>";
			echo "<thead>";
			echo "<tr><th>Name</th><th>Type</th><th>Cost</th><th>Comments</th>";
			echo "</thead>";
			echo "<tbody>";
			while($stmt2->fetch()) {
				echo "<tr>";
				echo("<td>" . "<a href='http://plato.cs.virginia.edu/~jjl9ve/final/img/cube_imgs/" 
					. $name . ".jpg'>" . $name . "<div id=cardImage><img src='http://plato.cs.virginia.edu/~jjl9ve/final/img/cube_imgs/" 
					. $name . ".jpg' style=height:50px; width:50px>" . "</div></a></td>\n");
				echo("<td>" . $type . "</td>\n");
				echo("<td>" . "<div id=cost>$cost</div></td>\n");
				echo("<td>" . $comments . "</td>\n");
				echo "</tr>";
			}
			echo "</tbody>";
			echo "</table>";
		}
	}
}
	$comments = $_GET["comments"];
	$cardName = $_GET["trimmedCardName"];
	comments($comments, $cardName);
?>