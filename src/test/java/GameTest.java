import static org.junit.jupiter.api.Assertions.assertEquals;

import de.profschmergmann.models.Game;
import de.profschmergmann.models.Move;
import de.profschmergmann.models.Piece;
import de.profschmergmann.models.Position;
import java.util.Comparator;
import java.util.concurrent.ThreadLocalRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GameTest {

  private Game g;

  @BeforeEach
  public void setup() {
    this.g = new Game();
  }

  @Test
  @DisplayName("Move knight from b1 to a3 should work.")
  public void TestMoveKnightFromB1ToA3() {
    this.g.move(new Position('b', 1), new Position('a', 3));
    assertEquals(new Piece(Piece.PieceEnum.KNIGHT_W),
        this.g.getCurrentBoard().getPositions().get(new Position('a', 3)),
        "Knight is not at a3.");
  }

  @Test
  @DisplayName("Move Pawn from c2 to c3 should work.")
  public void testMovePawnFromC2toC3() {
    this.g.move(new Position('c', 2), new Position('c', 3));
    assertEquals(new Piece(Piece.PieceEnum.PAWN_W),
        this.g.getCurrentBoard().getPositions().get(new Position('c', 3)),
        "Pawn is not at c3.");
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

  @Test
  @DisplayName("Play random moves each.")
  public void testPlayRandomMovesEach() {
    System.out.println(this.g.getCurrentBoard());
    for (var i = 0; i < 8; i++) {
      var moves = this.g.getAvailableMoves().stream().toList();
      moves.stream()
          .sorted(Comparator.comparing(Move::from))
          .forEach(System.out::println);
      var r = ThreadLocalRandom.current().nextInt(0, moves.size());
      var move = moves.get(r);
      System.out.println(
          move.piece().identifier + (move.canAttack() ? " attacks from " : " moves from ") +
              move.from() + " to " + move.to());
      this.g.move(move.from(), move.to());
      System.out.println(this.g.getCurrentBoard());
    }
  }

}
