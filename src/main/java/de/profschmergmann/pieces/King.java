package de.profschmergmann.pieces;

public class King extends Piece {

  public King(PieceColor pieceColor) {
    super(pieceColor, PieceType.KING);
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
