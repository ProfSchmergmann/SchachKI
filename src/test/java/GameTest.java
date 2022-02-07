import de.profschmergmann.models.Game;
import de.profschmergmann.models.Move;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadLocalRandom;

public class GameTest {

	@Test
	@DisplayName("Play random moves each.")
	public void testPlayRandomMovesEach() {
		var g = new Game();
		System.out.println(g.getCurrentBoard());
		for (var i = 0; i < 8; i++) {
			var moves = g.getAvailableMoves().stream().toList();
			moves.stream().filter(Move::canAttack).forEach(System.out::println);
			var r = ThreadLocalRandom.current().nextInt(0, moves.size());
			var move = moves.stream().filter(Move::canAttack).findFirst().orElse(moves.get(r));
			System.out.println(move.piece().identifier + (move.canAttack() ? " moves from " : " attacks from ") +
					move.from() + " to " + move.to());
			g.move(move.from(), move.to());
			System.out.println(g.getCurrentBoard());
		}
	}

}
