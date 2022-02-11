package de.profschmergmann;

import de.profschmergmann.pieces.Piece;

/**
 * Record move for storing a possible or made move.
 *
 * @param start         the position where the move starts
 * @param end           the position where the move ends
 * @param piece         the moving piece
 * @param capturedPiece the captured piece if there is any
 */
public record Move(Position start, Position end, Piece piece, Piece capturedPiece) {

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    var move = (Move) o;
    return this.canAttack() == move.canAttack() &&
        this.start.equals(move.start) &&
        this.end.equals(move.end) &&
        this.piece.equals(move.piece) &&
        this.capturedPiece == move.capturedPiece;
  }

  @Override
  public String toString() {
    return (this.canAttack() ? "Can attack " : "Can move ")
        + this.start + " -> " + this.end + " with " + this.piece;
  }

  /**
   * If this Move can attack any other piece.
   *
   * @return if captured piece is not equal to null
   */
  public boolean canAttack() {
    return this.capturedPiece != null;
  }
}
