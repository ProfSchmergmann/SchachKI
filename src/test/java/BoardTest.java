import de.profschmergmann.Board;
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
  @DisplayName("Test if all pieces are at the right position.")
  public void testIfBlackKingIsAtRightStartingSpot() {
    System.out.println(this.b);
  }

}
