import static org.junit.jupiter.api.Assertions.assertEquals;

import de.profschmergmann.Board;
import de.profschmergmann.Game;
import de.profschmergmann.pieces.Piece.PieceColor;
import de.profschmergmann.players.ComputerPlayer;
import de.profschmergmann.players.Player;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
  @DisplayName("Test FEN record in and output.")
  public void testIfFENRecordOutputIsSameAsInput() {
    var g = new Game(new Player(PieceColor.W, new ComputerPlayer()),
        new Player(PieceColor.B, new ComputerPlayer()), STARTING_FEN);
    assertEquals(STARTING_FEN, g.getCurrentGameAsFENRecord(),
        "Current board as FEN record and given record are not the same!");
  }

  @Test
  @DisplayName("Test FEN record  output.")
  public void testIfFENRecordOutputIsRight() {
    var g = new Game(new Player(PieceColor.W, new ComputerPlayer()),
        new Player(PieceColor.B, new ComputerPlayer()), null);
    assertEquals(STARTING_FEN, g.getCurrentGameAsFENRecord(),
        "Current board as FEN record and given record are not the same!");
  }
}
