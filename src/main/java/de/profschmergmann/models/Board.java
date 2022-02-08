package de.profschmergmann.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Class board which represents the current board with all pieces.
 */
public class Board {

  private final HashMap<Position, Piece> positions = new HashMap<>();
  private final List<Piece> attackedBlackPieces = new ArrayList<>();
  private final List<Piece> attackedWhitePieces = new ArrayList<>();
  private Position enPassant;

  /**
   * Constructor without parameters which produces an initial chess field.
   */
  public Board() {
    this.initBoard();
    this.enPassant = null;
  }

  /**
   * Constructor which produces a chess field with a string decoded in FEN-Notation.
   *
   * @param FENRecordFigures the FEN-notation string, if null or empty an initial board will be
   *                         created
   */
  public Board(String FENRecordFigures) {
    if (FENRecordFigures == null || FENRecordFigures.isEmpty()) {
      this.initBoard();
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
   * @param from        the {@link Position} to move from
   * @param to          the {@link  Position} to move to
   * @param currentTeam the team to move
   * @return the resulting board
   * @throws IllegalArgumentException if the move is not possible due to some reasons
   */
  public Board move(Position from, Position to, Piece.Team currentTeam)
      throws IllegalArgumentException {
    if (!this.positions.containsKey(from)) {
      throw new IllegalArgumentException("Move not possible from: " + from);
    }
    if (this.positions.containsKey(to) && this.positions.get(to).getTeam() == currentTeam) {
      throw new IllegalArgumentException("Cannot attack same team from: " + from + " to: " + to);
    }
    if (this.getAvailableMoves(currentTeam)
        .contains(new Move(from, to, this.positions.get(from).getPieceEnum(), true)) ||
        this.getAvailableMoves(currentTeam)
            .contains(new Move(from, to, this.positions.get(from).getPieceEnum(), false))) {
      var pieceToMove = this.positions.get(from);
      var pieceToAttack = this.positions.get(to);
      if (pieceToAttack != null) {
        switch (currentTeam) {
          case WHITE -> this.attackedBlackPieces.add(pieceToAttack);
          case BLACK -> this.attackedWhitePieces.add(pieceToAttack);
        }
      }
      this.positions.remove(from);
      this.positions.put(to, pieceToMove);
      if ((pieceToMove.getPieceEnum().equals(Piece.PieceEnum.PAWN_W)
          || pieceToMove.getPieceEnum().equals(Piece.PieceEnum.PAWN_B)) &&
          Math.abs(from.rank() - to.rank()) == 2) {
        this.enPassant = new Position(from.file(),
            from.rank() + to.rank() / 2);
      }
      return this;
    }
    throw new IllegalArgumentException("Move from: " + from + " to: " + to + " not possible!");
  }

  public Map.Entry<Position, Piece> findPieceOnBoard(Piece.PieceEnum piece) {
    return this.positions
        .entrySet()
        .stream()
        .filter(positionPieceEntry ->
            positionPieceEntry.getValue().getPieceEnum().equals(piece))
        .findFirst()
        .orElse(null);
  }

  /**
   * Tries to compute all available moves for the given team.
   * TODO: Try if this method works faster with parallel streams
   * TODO: Add castling and en passant
   * TODO: What happens if pawn is at the end?
   *
   * @param team the team to compute the moves for
   * @return a HashSet of moves
   */
  public HashSet<Move> getAvailableMoves(Piece.Team team) {
    var set = new HashSet<Move>();
    for (Map.Entry<Position, Piece> entry : this.positions.entrySet()) {
      var fullPiece = entry.getValue();
      if (!fullPiece.getTeam().equals(team)) {
        continue;
      }
      var currentPos = entry.getKey();
      var piece = entry.getValue().getPieceEnum();
      char c;
      int i;
      if (piece == Piece.PieceEnum.BISHOP_W || piece == Piece.PieceEnum.BISHOP_B ||
          piece == Piece.PieceEnum.QUEEN_W || piece == Piece.PieceEnum.QUEEN_B) {

        c = (char) (currentPos.file() - 1);
        i = currentPos.rank() + 1;
        while (c >= 'a' && i <= 8) {
          if (this.addMoveToSetIfPossible(team, set, currentPos, piece, c, i)) {
            break;
          }
          c--;
          i++;
        }

        c = (char) (currentPos.file() + 1);
        i = currentPos.rank() + 1;
        while (c <= 'h' && i <= 8) {
          if (this.addMoveToSetIfPossible(team, set, currentPos, piece, c, i)) {
            break;
          }
          c++;
          i++;
        }

        c = (char) (currentPos.file() + 1);
        i = currentPos.rank() - 1;
        while (c <= 'h' && i >= 1) {
          if (this.addMoveToSetIfPossible(team, set, currentPos, piece, c, i)) {
            break;
          }
          c++;
          i--;
        }
        c = (char) (currentPos.file() - 1);
        i = currentPos.rank() - 1;
        while (c >= 'a' && i >= 1) {
          if (this.addMoveToSetIfPossible(team, set, currentPos, piece, c, i)) {
            break;
          }
          c--;
          i--;
        }

      }
      if (piece == Piece.PieceEnum.KING_W || piece == Piece.PieceEnum.KING_B) {
        for (c = 'a'; c <= 'h'; c++) {
          for (i = 1; i <= 8; i++) {
            if (currentPos.file() - 1 == c && currentPos.rank() + 1 == i ||
                currentPos.file() == c && currentPos.rank() + 1 == i ||
                currentPos.file() + 1 == c && currentPos.rank() + 1 == i ||
                currentPos.file() + 1 == c && currentPos.rank() == i ||
                currentPos.file() - 1 == c && currentPos.rank() - 1 == i ||
                currentPos.file() == c && currentPos.rank() == i - 1 ||
                currentPos.file() + 1 == c && currentPos.rank() - 1 == i ||
                currentPos.file() - 1 == c && currentPos.rank() == i) {
              this.addMoveToSetIfPossible(team, set, currentPos, piece, c, i);
            }
          }
        }
      }
      if (piece == Piece.PieceEnum.KNIGHT_W || piece == Piece.PieceEnum.KNIGHT_B) {
        for (c = 'a'; c <= 'h'; c++) {
          for (i = 1; i <= 8; i++) {
            if (currentPos.file() - 2 == c && currentPos.rank() + 1 == i ||
                currentPos.file() - 1 == c && currentPos.rank() + 2 == i ||
                currentPos.file() + 1 == c && currentPos.rank() + 2 == i ||
                currentPos.file() + 2 == c && currentPos.rank() + 1 == i ||
                currentPos.file() + 2 == c && currentPos.rank() - 1 == i ||
                currentPos.file() + 1 == c && currentPos.rank() - 2 == i ||
                currentPos.file() - 1 == c && currentPos.rank() - 2 == i ||
                currentPos.file() - 2 == c && currentPos.rank() - 1 == i) {
              this.addMoveToSetIfPossible(team, set, currentPos, piece, c, i);
            }
          }
        }
      }
      if (piece == Piece.PieceEnum.PAWN_W || piece == Piece.PieceEnum.PAWN_B) {
        switch (team) {
          case WHITE -> {
            if (currentPos.rank() + 1 <= 8) {
              this.addMoveToSetIfPossible(team, set, currentPos, piece, currentPos.file(),
                  currentPos.rank() + 1);
              if (currentPos.rank() == 2) {
                this.addMoveToSetIfPossible(team, set, currentPos, piece, currentPos.file(),
                    currentPos.rank() + 2);
              }
              if (currentPos.file() + 1 <= 'h') {
                this.addMoveToSetIfPossible(team, set, currentPos, piece,
                    (char) (currentPos.file() + 1), currentPos.rank() + 1);
              }
              if (currentPos.file() - 1 >= 'a') {
                this.addMoveToSetIfPossible(team, set, currentPos, piece,
                    (char) (currentPos.file() - 1), currentPos.rank() + 1);
              }
            }
          }
          case BLACK -> {
            if (currentPos.rank() - 1 >= 1) {
              this.addMoveToSetIfPossible(team, set, currentPos, piece, currentPos.file(),
                  currentPos.rank() - 1);
              if (currentPos.rank() == 7) {
                this.addMoveToSetIfPossible(team, set, currentPos, piece, currentPos.file(),
                    currentPos.rank() - 2);
              }
              if (currentPos.file() + 1 <= 'h') {
                this.addMoveToSetIfPossible(team, set, currentPos, piece,
                    (char) (currentPos.file() + 1), currentPos.rank() - 1);
              }
              if (currentPos.file() - 1 >= 'a') {
                this.addMoveToSetIfPossible(team, set, currentPos, piece,
                    (char) (currentPos.file() - 1), currentPos.rank() - 1);
              }
            }
          }
        }
      }
      if (piece == Piece.PieceEnum.ROOK_W || piece == Piece.PieceEnum.ROOK_B ||
          piece == Piece.PieceEnum.QUEEN_W || piece == Piece.PieceEnum.QUEEN_B) {

        c = (char) (currentPos.file() - 1);
        while (c >= 'a') {
          if (this.addMoveToSetIfPossible(team, set, currentPos, piece, c, currentPos.rank())) {
            break;
          }
          c--;
        }

        i = currentPos.rank() + 1;
        while (i <= 8) {
          if (this.addMoveToSetIfPossible(team, set, currentPos, piece, currentPos.file(), i)) {
            break;
          }
          i++;
        }

        c = (char) (currentPos.file() + 1);
        while (c <= 'h') {
          if (this.addMoveToSetIfPossible(team, set, currentPos, piece, c, currentPos.rank())) {
            break;
          }
          c++;
        }

        i = currentPos.rank() - 1;
        while (i >= 1) {
          if (this.addMoveToSetIfPossible(team, set, currentPos, piece, currentPos.file(), i)) {
            break;
          }
          i--;
        }

      }
    }
    return set;
  }

  /**
   * Helper method which adds the move to the set if it is possible.
   *
   * @param team       the team to move with
   * @param set        the set where the move should be added to
   * @param currentPos the current position
   * @param piece      the piece to move
   * @param c          the file
   * @param i          the rank
   * @return true if it worked, else false
   */
  private boolean addMoveToSetIfPossible(Piece.Team team, HashSet<Move> set, Position currentPos,
      Piece.PieceEnum piece, char c, int i) {
    var neighbourPos = new Position(c, i);
    if (this.enPassantPossible() && neighbourPos.equals(this.enPassant)) {
      set.add(new Move(currentPos, neighbourPos, piece, true));
      return true;
    }
    var pawn = piece.equals(Piece.PieceEnum.PAWN_B) ||
        piece.equals(Piece.PieceEnum.PAWN_W);
    if (this.positions.containsKey(neighbourPos)) {
      if (pawn && currentPos.file() == c) {
        return false;
      }
      if (this.positions.get(neighbourPos).getTeam() != team) {
        set.add(new Move(currentPos, neighbourPos, piece, true));
      }
      return true;
    }
    if (pawn && currentPos.file() != c) {
      return false;
    }
    set.add(new Move(currentPos, neighbourPos, piece, false));
    return false;
  }
}
