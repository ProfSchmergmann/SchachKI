import de.profschmergmann.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoveTest {

	private Board b;

	@BeforeEach
	public void setUp() {
		this.b = new Board();
	}

	@Test
	@DisplayName("Move knight from b1 to a3 should work.")
	public void TestMoveKnightFromB1ToA3() {
		this.b.move(new Position('b', 1), new Position('a', 3), Piece.Team.WHITE);
		assertEquals(new Piece(Piece.PieceEnum.KNIGHT_W),
				this.b.getPositions().get(new Position('a', 3)),
				"Knight is not at a3.");
	}

	@Test
	@DisplayName("Move Pawn from c2 to c3 should work.")
	public void testMovePawnFromC2toC3() {
		this.b.move(new Position('c', 2), new Position('c', 3), Piece.Team.WHITE);
		assertEquals(new Piece(Piece.PieceEnum.PAWN_W),
				this.b.getPositions().get(new Position('c', 3)),
				"Pawn is not at c3.");
	}

	@Test
	@DisplayName("Play random two moves each.")
	public void testPlayRandom2MovesEach() {
		var g = new Game();
		for (var i = 0; i < 6; i++) {
			System.out.println(g.getCurrentBoard());
			var moves = g.getAvailableMoves().stream().toList();
			moves.forEach(System.out::println);
			var r = ThreadLocalRandom.current().nextInt(0, moves.size());
			var move = moves.get(r);
			g.move(move.from(), move.to());
			System.out.println(move.piece().identifier + " moves from " + move.from() + " to " + move.to());
		}
	}

}
