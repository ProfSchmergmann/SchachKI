package de.profschmergmann.sad.pieces;

import de.profschmergmann.sad.Position;
import java.util.HashSet;

public class King extends Piece {

  public King(PieceColor pieceColor) {
    super(pieceColor, PieceType.KING);
  }

  @Override
  public HashSet<Position> possibleMoves() {
    return null;
  }

  @Override
  public HashSet<Position> positionsToAttack() {
    return null;
  }

  @Override
  public HashSet<Position> captureFreeMoves() {
    return null;
  }

  /**
   * If the current king is captured.
   * TODO!
   *
   * @return true or false
   */
  public boolean isCaptured() {
    return false;
  }

  @Override
  public String toString() {
    return this.pieceColor == PieceColor.W ? "K" : "k";
  }
}
