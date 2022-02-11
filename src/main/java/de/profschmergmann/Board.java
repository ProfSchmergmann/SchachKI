package de.profschmergmann;

import de.profschmergmann.pieces.Bishop;
import de.profschmergmann.pieces.King;
import de.profschmergmann.pieces.Knight;
import de.profschmergmann.pieces.Pawn;
import de.profschmergmann.pieces.Piece;
import de.profschmergmann.pieces.Piece.PieceColor;
import de.profschmergmann.pieces.Piece.PieceType;
import de.profschmergmann.pieces.Queen;
import de.profschmergmann.pieces.Rook;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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
  private Position posKingW;
  private Position posKingB;

  //region Constructors
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
  //endregion

  //region Getters and Setters
  /**
   * Returns all positions.
   *
   * @return the positions as HashMap.
   */
  public HashMap<Position, Piece> getPositions() {
    return this.positions;
  }

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

  //endregion

  //region Initializers
  /**
   * Method which produces a chess field with a string decoded in FEN-Notation.
   *
   * @param FENRecordFigures the FEN-notation string, if null or empty an initial board will be
   *                         created
   */
  private void initBoard(String FENRecordFigures) {
    if (FENRecordFigures == null || FENRecordFigures.isEmpty()) {
      new Board();
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
          if (piece.getPieceType().equals(PieceType.KING)) {
            switch (piece.getPieceColor()) {
              case B -> this.posKingB = pos;
              case W -> this.posKingW = pos;
            }
          }
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

  //endregion

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

  /**
   * Tries to move from the given position to the given position with the given team.
   *
   * @param from the {@link Position} to move from
   * @param to   the {@link  Position} to move to
   * @return the resulting board
   */
  public Move move(Position from, Position to) {
    var move = this.getAvailableMoves()
        .stream()
        .filter(move1 -> move1.start().equals(from) && move1.end().equals(to))
        .findFirst()
        .orElse(null);

    if (move == null) {
      LOGGER.log(Level.WARNING, "There is no move available from: " + from + " to: " + to + "!");
      return null;
    }

    var pieceToMove = this.positions.get(from);

    //region en passant handling
    if (pieceToMove.getPieceType().equals(PieceType.PAWN)) {
      if (move.canAttack()) {
        if (to.equals(this.enPassant)) {
          this.enPassant = null;
          switch (pieceToMove.getPieceColor()) {
            case W -> this.positions.remove(new Position(this.enPassant.file(), 5));
            case B -> this.positions.remove(new Position(this.enPassant.file(), 4));
          }
        }
      } else if (Math.abs(from.rank() - to.rank()) == 2) {
        this.enPassant = new Position(from.file(), from.rank() + to.rank() / 2);
      }
      this.halfMoves++;
    }
    //endregion

    //region castling handling
    if (pieceToMove.getPieceType().equals(PieceType.KING)) {
      if (pieceToMove.isMoved()) {
        switch (pieceToMove.getPieceColor()) {
          case B -> {
            this.setBlackCanCastleKingSide(false);
            this.setBlackCanCastleQueenSide(false);
          }
          case W -> {
            this.setWhiteCanCastleKingSide(false);
            this.setWhiteCanCastleQueenSide(false);
          }
        }
      }
    }
    var rookW = new Rook(PieceColor.W);
    if (this.whiteCanCastleQueenSide &&
        !this.positions.getOrDefault(new Position('a', 1), null).equals(rookW)) {
      this.setWhiteCanCastleQueenSide(false);
    }
    if (this.whiteCanCastleKingSide &&
        !this.positions.getOrDefault(new Position('h', 1), null).equals(rookW)) {
      this.setWhiteCanCastleKingSide(false);
    }
    var rookB = new Rook(PieceColor.B);
    if (this.blackCanCastleQueenSide &&
        !this.positions.getOrDefault(new Position('a', 8), null).equals(rookB)) {
      this.setBlackCanCastleQueenSide(false);
    }
    if (this.blackCanCastleKingSide &&
        !this.positions.getOrDefault(new Position('h', 8), null).equals(rookB)) {
      this.setBlackCanCastleKingSide(false);
    }
    //endregion

    //region check handling
    if (pieceToMove.getPieceType().equals(PieceType.KING)) {
      switch (pieceToMove.getPieceColor()) {
        case B -> this.blackChecked = false;
        case W -> this.whiteChecked = false;
      }
    }
    //endregion

    this.positions.remove(from);
    pieceToMove.setMoved();
    this.positions.put(to, pieceToMove);
    this.fullMoves++;

    this.currentTeam = this.currentTeam == PieceColor.W ? PieceColor.B : PieceColor.W;
    return move;
  }

  /**
   * Tries to compute all available moves for the current team.
   *
   * @return a HashSet of moves
   */
  public HashSet<Move> getAvailableMoves() {
    if (this.movesGeneratorCounter != this.fullMoves) {
      this.availableMoves = this.getAllAvailableMoves(this.currentTeam);
      this.movesGeneratorCounter++;
    }
    return this.availableMoves;
  }

  /**
   * Helper to compute all available moves for the current team.
   * TODO: Add castling
   * TODO: What happens if pawn is at the end?
   *
   * @return a HashSet of moves
   */
  private HashSet<Move> getAllAvailableMoves(PieceColor team) {
    var set = new HashSet<Move>();
    this.positions.entrySet()
        .stream()
        .filter(entry -> entry.getValue().getPieceColor().equals(team))
        .forEach(entry -> {
          var currentPos = entry.getKey();
          var piece = entry.getValue();
          char c;
          int i;
          Move move;
          switch (piece.getPieceType()) {
            case KING -> {
              for (c = 'a'; c <= 'h'; c++) {
                for (i = 1; i <= 8; i++) {
                  if ((char) (currentPos.file() - 1) == c && currentPos.rank() + 1 == i ||
                      currentPos.file() == c && currentPos.rank() + 1 == i ||
                      (char) (currentPos.file() + 1) == c && currentPos.rank() + 1 == i ||
                      (char) (currentPos.file() + 1) == c && currentPos.rank() == i ||
                      (char) (currentPos.file() - 1) == c && currentPos.rank() - 1 == i ||
                      currentPos.file() == c && currentPos.rank() == i - 1 ||
                      (char) (currentPos.file() + 1) == c && currentPos.rank() - 1 == i ||
                      (char) (currentPos.file() - 1) == c && currentPos.rank() == i) {
                    move = this.createMoveIfPossible(currentPos, piece,
                        new Position(c, i));
                    if (move != null) {
                      set.add(move);
                    }
                  }
                }
              }
            }
            //region Pawn
            case PAWN -> {
              var positionsToAdd = new HashSet<Position>();
              c = currentPos.file();
              if (piece.getPieceColor().equals(PieceColor.W)) {
                i = currentPos.rank() + 1;
                if (i <= 8) {
                  positionsToAdd.add(new Position(c, i));
                  if (currentPos.rank() == 2) {
                    positionsToAdd.add(new Position(c, 3));
                  }
                }
              } else {
                i = currentPos.rank() - 1;
                if (i >= 1) {
                  positionsToAdd.add(new Position(c, i));
                  if (currentPos.rank() == 7) {
                    positionsToAdd.add(new Position(c, 6));
                  }
                }
              }
              if (i >= 1 && i <= 8) {
                c = (char) (currentPos.file() + 1);
                if (c <= 'h') {
                  positionsToAdd.add(new Position(c, i));
                }
                c = (char) (currentPos.file() - 1);
                if (c >= 'a') {
                  positionsToAdd.add(new Position(c, i));
                }
                for (var position : positionsToAdd) {
                  move = this.createMoveIfPossible(currentPos, piece, position);
                  if (move != null) {
                    set.add(move);
                  }
                }
              }
            }
            //endregion
            case KNIGHT -> {
              for (c = 'a'; c <= 'h'; c++) {
                for (i = 1; i <= 8; i++) {
                  if ((char) (currentPos.file() - 2) == c && currentPos.rank() + 1 == i ||
                      (char) (currentPos.file() - 1) == c && currentPos.rank() + 2 == i ||
                      (char) (currentPos.file() + 1) == c && currentPos.rank() + 2 == i ||
                      (char) (currentPos.file() + 2) == c && currentPos.rank() + 1 == i ||
                      (char) (currentPos.file() + 2) == c && currentPos.rank() - 1 == i ||
                      (char) (currentPos.file() + 1) == c && currentPos.rank() - 2 == i ||
                      (char) (currentPos.file() - 1) == c && currentPos.rank() - 2 == i ||
                      (char) (currentPos.file() - 2) == c && currentPos.rank() - 1 == i) {
                    move = this.createMoveIfPossible(currentPos, piece, new Position(c, i));
                    if (move != null) {
                      set.add(move);
                    }
                  }
                }
              }
            }
            case BISHOP, ROOK, QUEEN -> {
              //region Bishop, Queen
              if (piece.getPieceType().equals(PieceType.BISHOP) ||
                  piece.getPieceType().equals(PieceType.QUEEN)) {
                c = (char) (currentPos.file() - 1);
                i = currentPos.rank() + 1;
                while (c >= 'a' && i <= 8) {
                  move = this.createMoveIfPossible(currentPos, piece, new Position(c, i));
                  if (move != null) {
                    set.add(move);
                    if (move.canAttack()) {
                      break;
                    }
                  } else {
                    break;
                  }
                  c--;
                  i++;
                }
                c = (char) (currentPos.file() + 1);
                i = currentPos.rank() + 1;
                while (c <= 'h' && i <= 8) {
                  move = this.createMoveIfPossible(currentPos, piece, new Position(c, i));
                  if (move != null) {
                    set.add(move);
                    if (move.canAttack()) {
                      break;
                    }
                  } else {
                    break;
                  }
                  c++;
                  i++;
                }
                c = (char) (currentPos.file() + 1);
                i = currentPos.rank() - 1;
                while (c <= 'h' && i >= 1) {
                  move = this.createMoveIfPossible(currentPos, piece, new Position(c, i));
                  if (move != null) {
                    set.add(move);
                    if (move.canAttack()) {
                      break;
                    }
                  } else {
                    break;
                  }
                  c++;
                  i--;
                }
                c = (char) (currentPos.file() - 1);
                i = currentPos.rank() - 1;
                while (c >= 'a' && i >= 1) {
                  move = this.createMoveIfPossible(currentPos, piece, new Position(c, i));
                  if (move != null) {
                    set.add(move);
                    if (move.canAttack()) {
                      break;
                    }
                  } else {
                    break;
                  }
                  c--;
                  i--;
                }
              }
              //endregion
              //region Rook, Queen
              if (piece.getPieceType().equals(PieceType.ROOK) ||
                  piece.getPieceType().equals(PieceType.QUEEN)) {
                c = (char) (currentPos.file() - 1);
                i = currentPos.rank();
                while (c >= 'a') {
                  move = this.createMoveIfPossible(currentPos, piece, new Position(c, i));
                  if (move != null) {
                    set.add(move);
                    if (move.canAttack()) {
                      break;
                    }
                  } else {
                    break;
                  }
                  c--;
                }
                i = currentPos.rank() + 1;
                c = currentPos.file();
                while (i <= 8) {
                  move = this.createMoveIfPossible(currentPos, piece, new Position(c, i));
                  if (move != null) {
                    set.add(move);
                    if (move.canAttack()) {
                      break;
                    }
                  } else {
                    break;
                  }
                  i++;
                }
                c = (char) (currentPos.file() + 1);
                i = currentPos.rank();
                while (c <= 'h') {
                  move = this.createMoveIfPossible(currentPos, piece, new Position(c, i));
                  if (move != null) {
                    set.add(move);
                    if (move.canAttack()) {
                      break;
                    }
                  } else {
                    break;
                  }
                  c++;
                }
                c = currentPos.file();
                i = currentPos.rank() - 1;
                while (i >= 1) {
                  move = this.createMoveIfPossible(currentPos, piece, new Position(c, i));
                  if (move != null) {
                    set.add(move);
                    if (move.canAttack()) {
                      break;
                    }
                  } else {
                    break;
                  }
                  i--;
                }
              }
              //endregion
            }
          }
        });
    //region check handling
    var posKing = this.currentTeam.equals(PieceColor.W) ? this.posKingW : this.posKingB;
    var attackingPositions = set.stream()
        .filter(Move::canAttack)
        .map(Move::end)
        .filter(position -> position.equals(posKing))
        .toList();
    if (!attackingPositions.isEmpty()) {
      switch (team) {
        case W -> this.blackChecked = true;
        case B -> this.whiteChecked = true;
      }
    }
    if (team.equals(PieceColor.W) && this.whiteChecked
        || team.equals(PieceColor.B) && this.blackChecked) {
      set.removeIf(move -> !move.piece().getPieceType().equals(PieceType.KING));
      set.removeIf(move -> attackingPositions.contains(move.end()));
      if (!set.isEmpty()) {
        LOGGER.log(Level.WARNING, posKing + " is checked by\n");
        set.forEach(move -> LOGGER.log(Level.WARNING, move.toString()));
      }
    }
    //endregion
    return set;
  }

  /**
   * Finds a specific piece on the board if it exists.
   *
   * @param piece the piece to find
   * @return the position piece entry if found or null if not found
   */
  public Map.Entry<Position, Piece> findPieceOnBoard(Piece piece) {
    return this.positions
        .entrySet()
        .stream()
        .filter(positionPieceEntry ->
            positionPieceEntry.getValue().equals(piece))
        .findFirst()
        .orElse(null);
  }

  /**
   * Helper method which creates the move if it is possible.
   *
   * @param currentPos   the current position
   * @param piece        the piece to move
   * @param neighbourPos the neighbour position to check
   * @return the move or null
   */
  private Move createMoveIfPossible(Position currentPos, Piece piece, Position neighbourPos) {
    var pawn = piece.getPieceType().equals(PieceType.PAWN);
    if (pawn && this.enPassantPossible() && neighbourPos.equals(this.enPassant)) {
      return new Move(currentPos, neighbourPos, piece, this.positions.get(this.enPassant));
    }
    if (this.positions.containsKey(neighbourPos)) {
      if (pawn && currentPos.file() == neighbourPos.file()) {
        return null;
      }
      if (!this.positions.get(neighbourPos).getPieceColor().equals(this.currentTeam)) {
        return new Move(currentPos, neighbourPos, piece, this.positions.get(neighbourPos));
      }
      return null;
    } else {
      if (pawn && currentPos.file() != neighbourPos.file()) {
        return null;
      }
      return new Move(currentPos, neighbourPos, piece, null);
    }
  }

}
