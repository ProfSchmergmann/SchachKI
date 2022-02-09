package de.profschmergmann.models;

import de.profschmergmann.models.Piece.Team;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class game for holding multiple games as an ArrayList.
 */
public class Game {

  private static final Logger LOGGER = Logger.getLogger(Game.class.getName());
  private final List<Board> boards = new ArrayList<>();

  /**
   * Constructor for a game where white starts.
   */
  public Game() {
    var b = new Board();
    b.setCurrentTeam(Team.WHITE);
    b.setWhiteCanCastleKingSide(true);
    b.setWhiteCanCastleQueenSide(true);
    b.setBlackCanCastleKingSide(true);
    b.setBlackCanCastleQueenSide(true);
    b.setEnPassant(null);
    b.setHalfMoves(0);
    b.setFullMoves(0);
    this.boards.add(b);
  }

  /**
   * Constructor for a game with a given FEN-record. If the FEN-record is the starting record you
   * can also use the constructor without any parameters.
   *
   * @param FENRecord a String parsed as
   *                  <a href="https://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation">FEN-Notation</a>
   */
  public Game(String FENRecord) {
    var splitRecord = FENRecord.split("\s");
    var b = new Board(splitRecord[0]);
    b.setCurrentTeam(splitRecord[1].equals("w") ? Piece.Team.WHITE : Piece.Team.BLACK);
    if (splitRecord[2].equals("-")) {
      b.setWhiteCanCastleKingSide(false);
      b.setWhiteCanCastleQueenSide(false);
      b.setBlackCanCastleKingSide(false);
      b.setBlackCanCastleQueenSide(false);
    } else {
      if (splitRecord[2].contains("K")) {
        b.setWhiteCanCastleKingSide(true);
      }
      if (splitRecord[2].contains("Q")) {
        b.setWhiteCanCastleQueenSide(true);
      }
      if (splitRecord[2].contains("k")) {
        b.setBlackCanCastleKingSide(true);
      }
      if (splitRecord[2].contains("q")) {
        b.setBlackCanCastleQueenSide(true);
      }
    }
    if (!splitRecord[3].equals("-")) {
      b.setEnPassant(new Position(splitRecord[3].charAt(0),
          Integer.parseInt(String.valueOf(splitRecord[3].charAt(1)))));
    }
    b.setHalfMoves(Integer.parseInt(String.valueOf(splitRecord[4].charAt(0))));
    b.setFullMoves(Integer.parseInt(String.valueOf(splitRecord[5].charAt(0))));
    this.boards.add(b);
    LOGGER.log(Level.INFO, "Created new game with FEN-Record: " + FENRecord);
  }

  public Board getCurrentBoard() {
    return this.boards.get(this.boards.size() - 1);
  }

  public Piece.Team getCurrentTeam() {
    return this.getCurrentBoard().getCurrentTeam();
  }

  /**
   * Method for computing if the king is checked on the current board.
   *
   * @return true if yes, else false
   */
  public boolean isChecked() {
    return this.getCurrentBoard().isChecked();
  }

  /**
   * Method for performing a move inside the current game.
   *
   * @param from the {@link Position} from where the move should be performed
   * @param to   the {@link Position} to where the move should be performed
   * @return true if the move worked, else false
   */
  public boolean move(Position from, Position to) {
    var newBoard = this.boards.get(this.boards.size() - 1).move(from, to);
    if (newBoard != null) {
      LOGGER.log(Level.FINE, "Moved a piece from " + from + " to: " + to);
      this.boards.add(newBoard);
      return true;
    }
    return false;
  }

  public HashSet<Move> getAvailableMoves() {
    return this.boards.get(this.boards.size() - 1).getAvailableMoves();
  }

  /**
   * Method for returning the current game as a
   * <a href="https://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation">FEN-Notation</a>.
   *
   * @return the current game as FEN Notation
   */
  public String getCurrentGameAsFENRecord() {
    var board = new Piece[8][8];
    var currentBoard = this.boards.get(this.boards.size() - 1);
    currentBoard.getPositions().forEach((position, piece) -> {
      var column = switch (position.file()) {
        case 'a' -> 0;
        case 'b' -> 1;
        case 'c' -> 2;
        case 'd' -> 3;
        case 'e' -> 4;
        case 'f' -> 5;
        case 'g' -> 6;
        case 'h' -> 7;
        default -> 8;
      };
      board[position.rank() - 1][column] = piece;
    });
    var res = new StringBuilder();
    var space = 0;
    for (var i = 7; i >= 0; i--) {
      space = 0;
      for (var j = 0; j < board[i].length; j++) {
        if (board[i][j] != null) {
          if (space > 0) {
            res.append(space);
            space = 0;
          }
          res.append(board[i][j].getPieceEnum().identifier);
        } else {
          space++;
        }
      }
      if (space > 0) {
        res.append(space);
      }
      res.append("/");
    }
    res.deleteCharAt(res.length() - 1)
        .append("\s")
        .append(currentBoard.getCurrentTeam().identifier)
        .append("\s");
    var castleTmp = "";
    if (currentBoard.canWhiteCastleKingSide()) {
      castleTmp += "K";
    }
    if (currentBoard.canWhiteCastleQueenSide()) {
      castleTmp += "Q";
    }
    if (currentBoard.canBlackCastleKingSide()) {
      castleTmp += "k";
    }
    if (currentBoard.canBlackCastleQueenSide()) {
      castleTmp += "q";
    }
    res.append(!castleTmp.isEmpty() ? castleTmp : "-")
        .append("\s")
        .append(currentBoard.enPassantPossible() ? currentBoard.getEnPassant() : "-")
        .append("\s")
        .append(currentBoard.getHalfMoves())
        .append("\s")
        .append(currentBoard.getFullMoves());
    return res.toString();
  }
}
