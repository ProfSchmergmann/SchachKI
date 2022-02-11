package de.profschmergmann.pieces;

public abstract class Piece {

  protected final PieceColor pieceColor;
  protected final PieceType pieceType;
  protected boolean moved;

  public Piece(PieceColor pieceColor, PieceType pieceType) {
    this.pieceColor = pieceColor;
    this.pieceType = pieceType;
    this.moved = false;
  }

  public PieceColor getPieceColor() {
    return this.pieceColor;
  }

  public PieceType getPieceType() {
    return this.pieceType;
  }

  public boolean isMoved() {
    return this.moved;
  }

  public void setMoved() {
    this.moved = true;
  }

  public abstract String toString();

  public enum PieceType {
    PAWN, KING, QUEEN, ROOK, BISHOP, KNIGHT
  }

  public enum PieceColor {
    B, W
  }
}
