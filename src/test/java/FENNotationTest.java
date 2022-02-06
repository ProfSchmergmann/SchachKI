import de.profschmergmann.models.Board;
import de.profschmergmann.models.Game;
import de.profschmergmann.models.Piece;
import de.profschmergmann.models.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Class for testing the FEN-Notation methods.
 */
public class FENNotationTest {

	public static final String STARTING_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

	@Test
	@DisplayName("Test starting FEN Position.")
	public void testReadStartingFENNotation() {
		var b = new Board("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
		assertEquals(b.toString(), new Board().toString(), "ToString methods of boards are not equal!");
	}

	@Test
	@DisplayName("Test FEN record with just two pieces.")
	public void testFENRecordWithTwoPieces() {
		var b = new Board("8/8/8/2k5/4K3/8/8/8");
		assertEquals(new Piece(Piece.PieceEnum.KING_B),
				b.getPositions().get(new Position('c', 5)),
				"Black king is not at c5!");
		assertEquals(new Piece(Piece.PieceEnum.KING_W),
				b.getPositions().get(new Position('e', 4)),
				"White king is not at e4!");
	}

	@Test
	@DisplayName("Test FEN record in and output.")
	public void testIfFENRecordOutputIsSameAsInput() {
		var g = new Game(STARTING_FEN);
		assertEquals(STARTING_FEN, g.getCurrentGameAsFENRecord(),
				"Current board as FEN record and given record are not the same!");
	}

	@Test
	@DisplayName("Test FEN record  output.")
	public void testIfFENRecordOutputIsRight() {
		var g = new Game();
		assertEquals(STARTING_FEN, g.getCurrentGameAsFENRecord(),
				"Current board as FEN record and given record are not the same!");
	}
}
