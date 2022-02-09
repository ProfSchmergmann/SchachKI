package de.profschmergmann.models;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class piece for holding a single chess peace with its team. This class also has a team and a
 * piece enum for returning SAN values.
 */
public class Piece {

  private static final Logger LOGGER = Logger.getLogger(Piece.class.getName());
  private final PieceEnum pieceEnum;
  private boolean hasMoved;

  /**
   * Constructor which sets the piece enum and the hasMoved property.
   */
  public Piece(PieceEnum pieceEnum) {
    this.pieceEnum = pieceEnum;
    this.hasMoved = false;
    LOGGER.log(Level.FINE, "Created new piece: " + pieceEnum);
  }

  /**
   * Getter for the hasMoved property.
   *
   * @return if the piece has already moved.
   */
  public boolean hasMoved() {
    return this.hasMoved;
  }

  /**
   * Setter for the hasMoved property.
   */
  public void setHasMoved() {
    this.hasMoved = true;
  }

  /**
   * ToString.
   *
   * @return the identifier of this piece.
   */
  public String toString() {
    return this.pieceEnum.identifier;
  }

  /**
   * If the piece belongs to white.
   *
   * @return white, if the identifier is upper case, black otherwise
   */
  private boolean isWhite() {
    return Character.isUpperCase(this.pieceEnum.identifier.toCharArray()[0]);
  }

  /**
   * Gets the Team.
   *
   * @return the team black or white
   */
  public Team getTeam() {
    return this.isWhite() ? Team.WHITE : Team.BLACK;
  }

  /**
   * Getter for the pieceEnum property.
   *
   * @return the pieceEnum
   */
  public PieceEnum getPieceEnum() {
    return this.pieceEnum;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    var piece = (Piece) o;
    return this.hasMoved == piece.hasMoved &&
        this.pieceEnum == piece.pieceEnum;
  }

  /**
   * Team enum with identifiers.
   */
  public enum Team {
    WHITE("w"),
    BLACK("b");

    public final String identifier;

    Team(String identifier) {
      this.identifier = identifier;
    }
  }

  /**
   * Piece enum with identifiers.
   */
  public enum PieceEnum {
    BISHOP_W("B"),
    BISHOP_B("b"),
    KING_W("K"),
    KING_B("k"),
    KNIGHT_W("N"),
    KNIGHT_B("n"),
    PAWN_W("P"),
    PAWN_B("p"),
    QUEEN_W("Q"),
    QUEEN_B("q"),
    ROOK_W("R"),
    ROOK_B("r");

    public final String identifier;

    PieceEnum(String identifier) {
      this.identifier = identifier;
    }
  }
}
