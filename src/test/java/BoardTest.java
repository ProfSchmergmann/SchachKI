import de.profschmergmann.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BoardTest {

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
	@DisplayName("Test if black King is at the right position.")
	public void testIfBlackKingIsAtRightStartingSpot() {
		var kingB = this.b.findPieceOnBoard(Piece.PieceEnum.KING_B);
		assertEquals(new Position('e', 8), kingB.getKey(), "Black king is not at e8!");
	}

	@Test
	@DisplayName("Test if enPassant Option shows up.")
	public void testIfEnPassantOptionWorks() {
		var g = new Game("8/8/8/2pP4/8/8/8/8 w - c6 1 1");
		var enPassantMove = g.getAvailableMoves()
		                     .stream()
		                     .filter(Move::canAttack)
		                     .findFirst()
		                     .orElse(null);
		assertEquals(new Move(new Position('d', 5),
						new Position('c', 6),
						Piece.PieceEnum.PAWN_W, true),
				enPassantMove,
				"There was no enPassant Move present!");
	}

}
