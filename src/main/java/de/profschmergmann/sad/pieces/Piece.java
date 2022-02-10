package de.profschmergmann.sad.pieces;

import de.profschmergmann.sad.Position;
import java.util.HashSet;

public abstract class Piece {

  protected final PieceColor pieceColor;
  protected final PieceType pieceType;
  private final boolean moved;

  public Piece(PieceColor pieceColor, PieceType pieceType) {
    this.pieceColor = pieceColor;
    this.pieceType = pieceType;
    this.moved = false;
  }

  /**
   * All possible moves.
   *
   * @return all possible moves
   */
  public abstract HashSet<Position> possibleMoves();

  /**
   * Positions the current piece can move at with attacking some other piece.
   *
   * @return the positions where an opponent piece can be attacked
   */
  public abstract HashSet<Position> positionsToAttack();

  /**
   * Positions the current piece can move at without being captured.
   *
   * @return the positions which are not under attack
   */
  public abstract HashSet<Position> captureFreeMoves();

  public abstract String toString();

  public enum PieceType {
    PAWN, KING, QUEEN, ROOK, BISHOP, KNIGHT
  }

  public enum PieceColor {
    B, W
  }
}
