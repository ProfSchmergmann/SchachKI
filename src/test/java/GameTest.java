import de.profschmergmann.Game;
import de.profschmergmann.pieces.Piece.PieceColor;
import de.profschmergmann.players.ComputerPlayer;
import de.profschmergmann.players.Player;
import java.util.concurrent.ThreadLocalRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GameTest {

  private Game g;

  @BeforeEach
  public void setup() {
    this.g = new Game(new Player(PieceColor.W, new ComputerPlayer()),
        new Player(PieceColor.B, new ComputerPlayer()), null);
  }

  @Test
  @DisplayName("Play random moves each.")
  public void testPlayRandomMovesEach() {
    System.out.println(this.g.getCurrentBoard());
    var moves = this.g.getCurrentBoard().getAvailableMoves().stream().toList();
    while (!moves.isEmpty()) {
      moves.forEach(System.out::println);
      var r = ThreadLocalRandom.current().nextInt(0, moves.size());
      var move = moves.get(r);
      System.out.println(
          move.piece() + (move.canAttack() ? " attacks from " : " moves from ") +
              move.start() + " to " + move.end());
      this.g.move(move.start(), move.end());
      System.out.println(this.g.getCurrentBoard());
      moves = this.g.getCurrentBoard().getAvailableMoves().stream().toList();
    }
  }

}
