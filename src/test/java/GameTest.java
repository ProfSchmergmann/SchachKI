import de.profschmergmann.models.Game;
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
  @DisplayName("Play random moves each.")
  public void testPlayRandomMovesEach() {
    System.out.println(this.g.getCurrentBoard());
    var moves = this.g.getAvailableMoves().stream().toList();
    while (!moves.isEmpty()) {
      moves.forEach(System.out::println);
      var r = ThreadLocalRandom.current().nextInt(0, moves.size());
      var move = moves.get(r);
      System.out.println(
          move.piece().identifier + (move.canAttack() ? " attacks from " : " moves from ") +
              move.from() + " to " + move.to());
      this.g.move(move.from(), move.to());
      System.out.println(this.g.getCurrentBoard());
      moves = this.g.getAvailableMoves().stream().toList();
    }
  }

}
