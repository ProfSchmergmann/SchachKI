package de.profschmergmann.pieces;

public class Queen extends Piece {

  public Queen(PieceColor pieceColor) {
    super(pieceColor, PieceType.QUEEN);
  }

  @Override
  public String toString() {
    return this.pieceColor == PieceColor.W ? "Q" : "q";
  }
}
