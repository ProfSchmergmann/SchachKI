package de.profschmergmann.sad.pieces;

import de.profschmergmann.sad.Position;
import java.util.HashSet;

public class Bishop extends Piece {

  public Bishop(PieceColor pieceColor) {
    super(pieceColor, PieceType.BISHOP);
  }

  @Override
  public HashSet<Position> possibleMoves() {
    return null;
  }

  @Override
  public HashSet<Position> positionsToAttack() {
    return null;
  }

  @Override
  public HashSet<Position> captureFreeMoves() {
    return null;
  }

  @Override
  public String toString() {
    return this.pieceColor == PieceColor.W ? "B" : "b";
  }
}
