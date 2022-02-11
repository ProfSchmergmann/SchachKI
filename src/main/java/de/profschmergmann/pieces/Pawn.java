package de.profschmergmann.pieces;

public class Pawn extends Piece {

  private final MoveDirection moveDirection;
  private final boolean promoted;
  private final Piece promoteTo;

  public Pawn(PieceColor pieceColor) {
    super(pieceColor, PieceType.PAWN);
    this.promoted = false;
    this.promoteTo = null;
    this.moveDirection = pieceColor == PieceColor.W ? MoveDirection.UP : MoveDirection.DOWN;
  }

  @Override
  public String toString() {
    return this.pieceColor == PieceColor.W ? "P" : "p";
  }

  public enum MoveDirection {
    UP, DOWN
  }
}
