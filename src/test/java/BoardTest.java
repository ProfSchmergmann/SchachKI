import static org.junit.jupiter.api.Assertions.assertEquals;

import de.profschmergmann.models.Board;
import de.profschmergmann.models.Piece;
import de.profschmergmann.models.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class BoardTest {

  private Board b;

  @BeforeEach
  public void setUp() {
    this.b = new Board();
  }

  @Test
  @DisplayName("Test if black King is at the right position.")
  public void testIfBlackKingIsAtRightStartingSpot() {
    var kingB = this.b.findPieceOnBoard(Piece.PieceEnum.KING_B);
    assertEquals(new Position('e', 8), kingB.getKey(), "Black king is not at e8!");
  }

}
