package de.profschmergmann.models;

import de.profschmergmann.models.Piece.PieceEnum;
import de.profschmergmann.models.Piece.Team;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class board which represents the current board with all pieces.
 */
public class Board {

  private static final Logger LOGGER = Logger.getLogger(Board.class.getName());
  private final HashMap<Position, Piece> positions = new HashMap<>();
  private Position enPassant;
  private Piece.Team currentTeam;
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

  //region Constructors

  /**
   * Constructor without parameters which produces an initial chess field.
   */
  public Board() {
    this.initBoard();
    this.currentTeam = Team.WHITE;
    this.whiteCanCastleKingSide = true;
    this.whiteCanCastleQueenSide = true;
    this.blackCanCastleKingSide = true;
    this.blackCanCastleQueenSide = true;
    this.halfMoves = 0;
    this.fullMoves = 0;
    this.availableMoves = this.getAllAvailableMoves(Team.WHITE);
    this.whiteChecked = false;
    this.blackChecked = false;
    LOGGER.log(Level.FINE, "Initialized new board with starting values.");
  }

  /**
   * Constructor which produces a chess field with a string decoded in FEN-Notation.
   *
   * @param FENRecordFigures the FEN-notation string, if null or empty an initial board will be
   *                         created
   */
  public Board(String FENRecordFigures) {
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
          case 'P' -> new Piece(Piece.PieceEnum.PAWN_W);
          case 'p' -> new Piece(Piece.PieceEnum.PAWN_B);
          case 'R' -> new Piece(Piece.PieceEnum.ROOK_W);
          case 'r' -> new Piece(Piece.PieceEnum.ROOK_B);
          case 'N' -> new Piece(Piece.PieceEnum.KNIGHT_W);
          case 'n' -> new Piece(Piece.PieceEnum.KNIGHT_B);
          case 'B' -> new Piece(Piece.PieceEnum.BISHOP_W);
          case 'b' -> new Piece(Piece.PieceEnum.BISHOP_B);
          case 'Q' -> new Piece(Piece.PieceEnum.QUEEN_W);
          case 'q' -> new Piece(Piece.PieceEnum.QUEEN_B);
          case 'K' -> new Piece(Piece.PieceEnum.KING_W);
          case 'k' -> new Piece(Piece.PieceEnum.KING_B);
          default -> null;
        };
        if (piece != null) {
          this.positions.put(pos, piece);
        }
      }
    }
    LOGGER.log(Level.FINE, "Set figures at board with FEN-Record: " + FENRecordFigures);
  }
  //endregion

  //region Getters and Setters

  /**
   * Getter for current team.
   *
   * @return the current team
   */
  public Team getCurrentTeam() {
    return this.currentTeam;
  }

  /**
   * Setter for current team.
   *
   * @param currentTeam the current team
   */
  public void setCurrentTeam(Team currentTeam) {
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

  /**
   * Initializes a starting chess game board.
   */
  private void initBoard() {
    // Pawns
    for (var c = 'a'; c <= 'h'; c++) {
      this.positions.put(new Position(c, 2), new Piece(Piece.PieceEnum.PAWN_W));
      this.positions.put(new Position(c, 7), new Piece(Piece.PieceEnum.PAWN_B));
    }

    // Rooks
    this.positions.put(new Position('a', 1), new Piece(Piece.PieceEnum.ROOK_W));
    this.positions.put(new Position('h', 1), new Piece(Piece.PieceEnum.ROOK_W));
    this.positions.put(new Position('a', 8), new Piece(Piece.PieceEnum.ROOK_B));
    this.positions.put(new Position('h', 8), new Piece(Piece.PieceEnum.ROOK_B));

    // Knights
    this.positions.put(new Position('b', 1), new Piece(Piece.PieceEnum.KNIGHT_W));
    this.positions.put(new Position('g', 1), new Piece(Piece.PieceEnum.KNIGHT_W));
    this.positions.put(new Position('b', 8), new Piece(Piece.PieceEnum.KNIGHT_B));
    this.positions.put(new Position('g', 8), new Piece(Piece.PieceEnum.KNIGHT_B));

    // Bishops
    this.positions.put(new Position('c', 1), new Piece(Piece.PieceEnum.BISHOP_W));
    this.positions.put(new Position('f', 1), new Piece(Piece.PieceEnum.BISHOP_W));
    this.positions.put(new Position('c', 8), new Piece(Piece.PieceEnum.BISHOP_B));
    this.positions.put(new Position('f', 8), new Piece(Piece.PieceEnum.BISHOP_B));

    // Queens
    this.positions.put(new Position('d', 1), new Piece(Piece.PieceEnum.QUEEN_W));
    this.positions.put(new Position('d', 8), new Piece(Piece.PieceEnum.QUEEN_B));

    // Kings
    this.positions.put(new Position('e', 1), new Piece(Piece.PieceEnum.KING_W));
    this.positions.put(new Position('e', 8), new Piece(Piece.PieceEnum.KING_B));
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

  /**
   * Tries to move from the given position to the given position with the given team.
   *
   * @param from the {@link Position} to move from
   * @param to   the {@link  Position} to move to
   * @return the resulting board
   */
  public Board move(Position from, Position to) {
    var move = this.getAvailableMoves()
        .stream()
        .filter(move1 -> move1.from().equals(from) && move1.to().equals(to))
        .findFirst()
        .orElse(null);

    if (move == null) {
      LOGGER.log(Level.WARNING, "There is no move available from: " + from + " to: " + to + "!");
      return null;
    }

    var pieceToMove = this.positions.get(from);

    //region en passant handling
    if ((pieceToMove.getPieceEnum().equals(Piece.PieceEnum.PAWN_W)
        || pieceToMove.getPieceEnum().equals(Piece.PieceEnum.PAWN_B))) {
      if (move.canAttack()) {
        if (to.equals(this.enPassant)) {
          this.enPassant = null;
          if (pieceToMove.getPieceEnum().equals(Piece.PieceEnum.PAWN_W)) {
            this.positions.remove(new Position(this.enPassant.file(), 5));
          } else {
            this.positions.remove(new Position(this.enPassant.file(), 4));
          }
        }
      } else if (Math.abs(from.rank() - to.rank()) == 2) {
        this.enPassant = new Position(from.file(), from.rank() + to.rank() / 2);
      }
      this.halfMoves++;
    }
    //endregion

    //region castling handling
    if (pieceToMove.getPieceEnum().equals(PieceEnum.KING_W) && pieceToMove.hasMoved()) {
      this.setWhiteCanCastleKingSide(false);
      this.setWhiteCanCastleQueenSide(false);
    }
    if (pieceToMove.getPieceEnum().equals(PieceEnum.KING_B) && pieceToMove.hasMoved()) {
      this.setBlackCanCastleKingSide(false);
      this.setBlackCanCastleQueenSide(false);
    }
    if (this.whiteCanCastleQueenSide && this.positions.containsKey(new Position('a', 1)) &&
        !this.positions.get(new Position('a', 1)).getPieceEnum().equals(PieceEnum.ROOK_W)) {
      this.setWhiteCanCastleQueenSide(false);
    }
    if (this.whiteCanCastleKingSide && this.positions.containsKey(new Position('h', 1)) &&
        !this.positions.get(new Position('h', 1)).getPieceEnum().equals(PieceEnum.ROOK_W)) {
      this.setWhiteCanCastleKingSide(false);
    }
    if (this.blackCanCastleQueenSide && this.positions.containsKey(new Position('a', 8)) &&
        !this.positions.get(new Position('a', 8)).getPieceEnum().equals(PieceEnum.ROOK_B)) {
      this.setBlackCanCastleQueenSide(false);
    }
    if (this.blackCanCastleKingSide && this.positions.containsKey(new Position('h', 8)) &&
        !this.positions.get(new Position('h', 8)).getPieceEnum().equals(PieceEnum.ROOK_B)) {
      this.setBlackCanCastleKingSide(false);
    }
    //endregion

    //region check handling
    if (pieceToMove.getPieceEnum().equals(PieceEnum.KING_B)) {
      this.blackChecked = false;
    }
    if (pieceToMove.getPieceEnum().equals(PieceEnum.KING_W)) {
      this.whiteChecked = false;
    }
    //endregion

    this.positions.remove(from);
    pieceToMove.hasMoved();
    this.positions.put(to, pieceToMove);
    this.fullMoves++;

    this.currentTeam = this.currentTeam == Team.WHITE ? Team.BLACK : Team.WHITE;
    return this;
  }

  /**
   * Finds a specific pieceEnum on the board if it exists.
   *
   * @param pieceEnum the piece enum to find
   * @return the position piece entry if found or null if not found
   */
  public Map.Entry<Position, Piece> findPieceOnBoard(Piece.PieceEnum pieceEnum) {
    return this.positions
        .entrySet()
        .stream()
        .filter(positionPieceEntry ->
            positionPieceEntry.getValue().getPieceEnum().equals(pieceEnum))
        .findFirst()
        .orElse(null);
  }

  /**
   * Getter for checking.
   *
   * @return true if the king is checked by any other piece, else false
   */
  public boolean isChecked() {
    if (this.currentTeam == Team.WHITE) {
      return this.whiteChecked;
    }
    return this.blackChecked;
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
  private HashSet<Move> getAllAvailableMoves(Team team) {
    var set = new HashSet<Move>();
    this.positions.entrySet()
        .stream()
        .filter(entry -> entry.getValue().getTeam().equals(team))
        .forEach(entry -> {
          var currentPos = entry.getKey();
          var pieceEnum = entry.getValue().getPieceEnum();
          char c;
          int i;
          Move move;
          switch (pieceEnum) {
            case KING_B, KING_W -> {
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
                    move = this.createMoveIfPossible(currentPos, pieceEnum, new Position(c, i));
                    if (move != null) {
                      set.add(move);
                    }
                  }
                }
              }
            }
            case PAWN_B, PAWN_W -> {
              var positionsToAdd = new HashSet<Position>();
              c = currentPos.file();
              if (pieceEnum.equals(PieceEnum.PAWN_W)) {
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
                for (Position position : positionsToAdd) {
                  move = this.createMoveIfPossible(currentPos, pieceEnum, position);
                  if (move != null) {
                    set.add(move);
                  }
                }
              }
            }
            case KNIGHT_B, KNIGHT_W -> {
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
                    move = this.createMoveIfPossible(currentPos, pieceEnum, new Position(c, i));
                    if (move != null) {
                      set.add(move);
                    }
                  }
                }
              }
            }
            case BISHOP_B, BISHOP_W, ROOK_B, ROOK_W, QUEEN_B, QUEEN_W -> {
              //region Bishop, Queen
              if (pieceEnum == PieceEnum.BISHOP_W || pieceEnum == PieceEnum.BISHOP_B ||
                  pieceEnum == PieceEnum.QUEEN_W || pieceEnum == PieceEnum.QUEEN_B) {
                c = (char) (currentPos.file() - 1);
                i = currentPos.rank() + 1;
                while (c >= 'a' && i <= 8) {
                  move = this.createMoveIfPossible(currentPos, pieceEnum, new Position(c, i));
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
                  move = this.createMoveIfPossible(currentPos, pieceEnum, new Position(c, i));
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
                  move = this.createMoveIfPossible(currentPos, pieceEnum, new Position(c, i));
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
                  move = this.createMoveIfPossible(currentPos, pieceEnum, new Position(c, i));
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
              if (pieceEnum == PieceEnum.ROOK_W || pieceEnum == PieceEnum.ROOK_B ||
                  pieceEnum == PieceEnum.QUEEN_W || pieceEnum == PieceEnum.QUEEN_B) {
                c = (char) (currentPos.file() - 1);
                i = currentPos.rank();
                while (c >= 'a') {
                  move = this.createMoveIfPossible(currentPos, pieceEnum, new Position(c, i));
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
                  move = this.createMoveIfPossible(currentPos, pieceEnum, new Position(c, i));
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
                  move = this.createMoveIfPossible(currentPos, pieceEnum, new Position(c, i));
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
                  move = this.createMoveIfPossible(currentPos, pieceEnum, new Position(c, i));
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
    var king = this.findPieceOnBoard(team == Team.WHITE ? PieceEnum.KING_B : PieceEnum.KING_W);
    var attackingPositions = set.stream()
        .filter(Move::canAttack)
        .map(Move::to)
        .filter(position -> position.equals(king.getKey()))
        .toList();
    if (!attackingPositions.isEmpty()) {
      if (team == Team.WHITE) {
        this.blackChecked = true;
      } else {
        this.whiteChecked = true;
      }
    }
    if (team == Team.WHITE && this.whiteChecked || team == Team.BLACK && this.blackChecked) {
      set.removeIf(move -> !move.piece().equals(king.getValue().getPieceEnum()));
      set.removeIf(move -> attackingPositions.contains(move.to()));
      if (!set.isEmpty()) {
        LOGGER.log(Level.WARNING, king.getValue().getPieceEnum() + " is checked by\n");
        set.forEach(move -> LOGGER.log(Level.WARNING, move.toString()));
      }
    }
    //endregion
    return set;
  }

  /**
   * Helper method which creates the move if it is possible.
   *
   * @param currentPos   the current position
   * @param piece        the piece to move
   * @param neighbourPos the neighbour position to check
   * @return the move or null
   */
  private Move createMoveIfPossible(Position currentPos, Piece.PieceEnum piece,
      Position neighbourPos) {
    var pawn = piece.equals(Piece.PieceEnum.PAWN_B) || piece.equals(Piece.PieceEnum.PAWN_W);
    if (pawn && this.enPassantPossible() && neighbourPos.equals(this.enPassant)) {
      return new Move(currentPos, neighbourPos, piece, true);
    }
    if (this.positions.containsKey(neighbourPos)) {
      if (pawn && currentPos.file() == neighbourPos.file()) {
        return null;
      }
      if (!this.positions.get(neighbourPos).getTeam().equals(this.currentTeam)) {
        return new Move(currentPos, neighbourPos, piece, true);
      }
      return null;
    } else {
      if (pawn && currentPos.file() != neighbourPos.file()) {
        return null;
      }
      return new Move(currentPos, neighbourPos, piece, false);
    }
  }
}
