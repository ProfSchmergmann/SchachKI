package de.profschmergmann.pieces;

public class Knight extends Piece {

  public Knight(PieceColor pieceColor) {
    super(pieceColor, PieceType.KNIGHT);
  }

  @Override
  public String toString() {
    return this.pieceColor == PieceColor.W ? "N" : "n";
  }
}
