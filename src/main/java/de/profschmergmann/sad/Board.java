package de.profschmergmann.sad;

import de.profschmergmann.sad.Game.Move;
import de.profschmergmann.sad.pieces.Bishop;
import de.profschmergmann.sad.pieces.King;
import de.profschmergmann.sad.pieces.Knight;
import de.profschmergmann.sad.pieces.Pawn;
import de.profschmergmann.sad.pieces.Piece;
import de.profschmergmann.sad.pieces.Piece.PieceColor;
import de.profschmergmann.sad.pieces.Queen;
import de.profschmergmann.sad.pieces.Rook;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Board {

  private static final Logger LOGGER = Logger.getLogger(Board.class.getName());
  private final HashMap<Position, HashSet<Piece>> squaresUnderAttack;
  private final HashMap<Position, Piece> positions;
  private Position enPassant;
  private PieceColor currentTeam;
  private boolean whiteCanCastleKingSide;
  private boolean whiteCanCastleQueenSide;
  private boolean blackCanCastleKingSide;
  private boolean blackCanCastleQueenSide;
  private int halfMoves;
  private int fullMoves;
  private HashSet<Move> availableMoves;
  private int movesGeneratorCounter;
  private boolean whiteChecked;
  private boolean blackChecked;

  //region Getters and Setters

  /**
   * Getter for current team.
   *
   * @return the current team
   */
  public PieceColor getCurrentTeam() {
    return this.currentTeam;
  }

  /**
   * Setter for current team.
   *
   * @param currentTeam the current team
   */
  public void setCurrentTeam(PieceColor currentTeam) {
    this.currentTeam = currentTeam;
  }

  /**
   * Getter for castling white kingside.
   *
   * @return can white castle kingside
   */
  public boolean canWhiteCastleKingSide() {
    return this.whiteCanCastleKingSide;
  }

  /**
   * Setter for castling white kingside.
   *
   * @param whiteCanCastleKingSide can white castle kingside
   */
  public void setWhiteCanCastleKingSide(boolean whiteCanCastleKingSide) {
    this.whiteCanCastleKingSide = whiteCanCastleKingSide;
  }


  /**
   * Getter for castling white queenside.
   *
   * @return can white castle queenside
   */
  public boolean canWhiteCastleQueenSide() {
    return this.whiteCanCastleQueenSide;
  }

  /**
   * Setter for castling white queenside.
   *
   * @param whiteCanCastleQueenSide can white castle queenside
   */
  public void setWhiteCanCastleQueenSide(boolean whiteCanCastleQueenSide) {
    this.whiteCanCastleQueenSide = whiteCanCastleQueenSide;
  }

  /**
   * Getter for castling black kingside.
   *
   * @return can black castle kingside
   */
  public boolean canBlackCastleKingSide() {
    return this.blackCanCastleKingSide;
  }

  /**
   * Setter for castling black kingside.
   *
   * @param blackCanCastleKingSide can black castle kingside
   */
  public void setBlackCanCastleKingSide(boolean blackCanCastleKingSide) {
    this.blackCanCastleKingSide = blackCanCastleKingSide;
  }

  /**
   * Getter for castling black queenside.
   *
   * @return can black castle queenside
   */
  public boolean canBlackCastleQueenSide() {
    return this.blackCanCastleQueenSide;
  }

  /**
   * Setter for castling black queenside.
   *
   * @param blackCanCastleQueenSide can black castle queenside
   */
  public void setBlackCanCastleQueenSide(boolean blackCanCastleQueenSide) {
    this.blackCanCastleQueenSide = blackCanCastleQueenSide;
  }

  /**
   * Getter for the half moves.
   *
   * @return the half moves
   */
  public int getHalfMoves() {
    return this.halfMoves;
  }

  /**
   * Setter for the half moves.
   *
   * @param halfMoves the half moves
   */
  public void setHalfMoves(int halfMoves) {
    this.halfMoves = halfMoves;
  }

  /**
   * Getter for the full moves.
   *
   * @return the full moves
   */
  public int getFullMoves() {
    return this.fullMoves;
  }

  /**
   * Setter for the full moves.
   *
   * @param fullMoves the full moves
   */
  public void setFullMoves(int fullMoves) {
    this.fullMoves = fullMoves;
  }

  /**
   * Checks weather an enPassant is possible.
   *
   * @return true if {@link this#enPassant} is set, else false
   */
  public boolean enPassantPossible() {
    return this.enPassant != null;
  }

  /**
   * Getter for the enPassant position.
   *
   * @return the position of enPassant.
   */
  public Position getEnPassant() {
    return this.enPassant;
  }

  /**
   * Sets the enPassant position.
   *
   * @param position the position of enPassant
   */
  public void setEnPassant(Position position) {
    this.enPassant = position;
  }

  /**
   * Returns all positions.
   *
   * @return the positions as HashMap.
   */
  public HashMap<Position, Piece> getPositions() {
    return this.positions;
  }
  //endregion

  public Board() {
    this.positions = new HashMap<>();
    this.squaresUnderAttack = new HashMap<>();
    this.initBoard();
    LOGGER.log(Level.FINE, "Initialized new chess starting board.");
  }

  /**
   * Constructor which produces a chess field with a string decoded in FEN-Notation.
   *
   * @param FENRecordFigures the FEN-notation string, if null or empty an initial board will be
   *                         created
   */
  public Board(String FENRecordFigures) {
    this.positions = new HashMap<>();
    this.squaresUnderAttack = new HashMap<>();
    this.initBoard(FENRecordFigures);
    LOGGER.log(Level.FINE, "Initialized new chess board with record: " + FENRecordFigures);
  }

  private void initBoard(String FENRecordFigures) {
    if (FENRecordFigures == null || FENRecordFigures.isEmpty()) {
      new de.profschmergmann.models.Board();
      return;
    }
    var lines = FENRecordFigures.split("/");
    for (var i = 0; i < 8; i++) {
      char c = 'a';
      for (var j = 0; j < lines[i].length(); j++, c++) {
        var pos = new Position(c, 8 - i);
        if (String.valueOf(lines[i].charAt(j)).matches("\\d")) {
          var v = Integer.parseInt(String.valueOf(lines[i].charAt(j)));
          if (v == 8) {
            break;
          }
          c += Integer.parseInt(String.valueOf(lines[i].charAt(j)));
          j++;
          if (j >= lines[i].length()) {
            break;
          }
          if (c >= 'h') {
            break;
          }
          pos = new Position(c, 8 - i);
        }
        var piece = switch (lines[i].charAt(j)) {
          case 'P' -> new Pawn(PieceColor.W);
          case 'p' -> new Pawn(PieceColor.B);
          case 'R' -> new Rook(PieceColor.W);
          case 'r' -> new Rook(PieceColor.B);
          case 'N' -> new Knight(PieceColor.W);
          case 'n' -> new Knight(PieceColor.B);
          case 'B' -> new Bishop(PieceColor.W);
          case 'b' -> new Bishop(PieceColor.B);
          case 'Q' -> new Queen(PieceColor.W);
          case 'q' -> new Queen(PieceColor.B);
          case 'K' -> new King(PieceColor.W);
          case 'k' -> new King(PieceColor.B);
          default -> null;
        };
        if (piece != null) {
          this.positions.put(pos, piece);
        }
      }
    }
    LOGGER.log(Level.FINE, "Set figures at board with FEN-Record: " + FENRecordFigures);
  }

  /**
   * Initializes a starting chess game board.
   */
  private void initBoard() {
    for (var c = 'a'; c <= 'h'; c++) {
      this.positions.put(new Position(c, 2), new Pawn(PieceColor.W));
      this.positions.put(new Position(c, 7), new Pawn(PieceColor.B));
    }
    this.positions.put(new Position('a', 1), new Rook(PieceColor.W));
    this.positions.put(new Position('h', 1), new Rook(PieceColor.W));
    this.positions.put(new Position('a', 8), new Rook(PieceColor.B));
    this.positions.put(new Position('h', 8), new Rook(PieceColor.B));
    this.positions.put(new Position('b', 1), new Knight(PieceColor.W));
    this.positions.put(new Position('g', 1), new Knight(PieceColor.W));
    this.positions.put(new Position('b', 8), new Knight(PieceColor.B));
    this.positions.put(new Position('g', 8), new Knight(PieceColor.B));
    this.positions.put(new Position('c', 1), new Bishop(PieceColor.W));
    this.positions.put(new Position('f', 1), new Bishop(PieceColor.W));
    this.positions.put(new Position('c', 8), new Bishop(PieceColor.B));
    this.positions.put(new Position('f', 8), new Bishop(PieceColor.B));
    this.positions.put(new Position('d', 1), new Queen(PieceColor.W));
    this.positions.put(new Position('d', 8), new Queen(PieceColor.B));
    this.positions.put(new Position('e', 1), new King(PieceColor.W));
    this.positions.put(new Position('e', 8), new King(PieceColor.B));
  }

  /**
   * Returns the current board as a formatted string for the console.
   *
   * @return the formatted string
   */
  public String toString() {
    var res = new StringBuilder("\n\s\s\s\s");
    for (var c = 'a'; c <= 'h'; c++) {
      res.append(c).append(" ");
    }
    res.append("\n");
    for (var i = 8; i >= 1; i--) {
      res.append(i).append(" | ");
      for (var c = 'a'; c <= 'h'; c++) {
        if (this.positions.containsKey(new Position(c, i))) {
          res.append(this.positions.get(new Position(c, i)));
        } else {
          res.append("\s");
        }
        res.append("\s");
        if (c == 'h') {
          res.append("|\s").append(i);
        }
      }
      res.append("\n");
    }
    res.append("\n");
    return res.toString();
  }

}
