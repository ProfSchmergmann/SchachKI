package de.profschmergmann.pieces;

public class Rook extends Piece {

  public Rook(PieceColor pieceColor) {
    super(pieceColor, PieceType.ROOK);
  }

  @Override
  public String toString() {
    return this.pieceColor == PieceColor.W ? "R" : "r";
  }
}
