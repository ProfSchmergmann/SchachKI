package de.profschmergmann.pieces;

public class Bishop extends Piece {

  public Bishop(PieceColor pieceColor) {
    super(pieceColor, PieceType.BISHOP);
  }

  @Override
  public String toString() {
    return this.pieceColor == PieceColor.W ? "B" : "b";
  }
}
