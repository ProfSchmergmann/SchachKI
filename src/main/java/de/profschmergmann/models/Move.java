package de.profschmergmann.models;

/**
 * Record move for storing a possible or made move.
 *
 * @param from      the {@link Position} from where the move is made
 * @param to        the {@link Position} to where the move is made
 * @param piece     the {@link Piece} to be moved
 * @param canAttack if the moving piece can attack
 */
public record Move(Position from, Position to, Piece.PieceEnum piece, boolean canAttack) {

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    Move move = (Move) o;
    return this.canAttack == move.canAttack &&
        this.from.equals(move.from) &&
        this.to.equals(move.to) &&
        this.piece == move.piece;
  }

  @Override
  public String toString() {
    return (this.canAttack ? "Can attack " : "Can move ")
        + this.from + " -> " + this.to + " with " + this.piece.identifier;
  }
}
