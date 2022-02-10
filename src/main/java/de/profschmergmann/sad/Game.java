package de.profschmergmann.sad;

import de.profschmergmann.sad.pieces.Piece;
import de.profschmergmann.sad.pieces.Piece.PieceColor;
import de.profschmergmann.sad.players.Player;
import java.util.ArrayList;
import java.util.List;

public class Game {

  private final Board currentBoard;
  private final List<Move> playedMoves;
  private final Player player1;
  private final Player player2;
  private final PieceColor turn;
  private Result result;
  private CheckStatus checkStatus;

  public Game(Player player1, Player player2) {
    this.player1 = player1;
    this.player2 = player2;
    this.playedMoves = new ArrayList<>();
    this.turn = Piece.PieceColor.W;
    this.result = null;
    this.checkStatus = CheckStatus.NONE;
    this.currentBoard = new Board();
  }

  public void addMove(Move move) {
    this.playedMoves.add(move);
  }

  public boolean isEnded() {
    return this.result != null;
  }

  public boolean isChecked() {
    return this.turn == Piece.PieceColor.W ?
        this.checkStatus.equals(CheckStatus.WHITE_IN_CHECK) :
        this.checkStatus.equals(CheckStatus.BLACK_IN_CHECK);
  }

  public boolean isCheckmated() {
    return this.turn == Piece.PieceColor.W ?
        this.checkStatus.equals(CheckStatus.WHITE_CHECKMATED) :
        this.checkStatus.equals(CheckStatus.BLACK_CHECKMATED);
  }

  /**
   * Method for returning the current game as a
   * <a href="https://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation">FEN-Notation</a>.
   *
   * @return the current game as FEN Notation
   */
  public String getCurrentGameAsFENRecord() {
    var board = new Piece[8][8];
    this.currentBoard.getPositions().forEach((position, piece) -> {
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
          res.append(board[i][j].toString());
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
        .append(this.currentBoard.getCurrentTeam().toString().toLowerCase())
        .append("\s");
    var castleTmp = "";
    if (this.currentBoard.canWhiteCastleKingSide()) {
      castleTmp += "K";
    }
    if (this.currentBoard.canWhiteCastleQueenSide()) {
      castleTmp += "Q";
    }
    if (this.currentBoard.canBlackCastleKingSide()) {
      castleTmp += "k";
    }
    if (this.currentBoard.canBlackCastleQueenSide()) {
      castleTmp += "q";
    }
    res.append(!castleTmp.isEmpty() ? castleTmp : "-")
        .append("\s")
        .append(this.currentBoard.enPassantPossible() ? this.currentBoard.getEnPassant() : "-")
        .append("\s")
        .append(this.currentBoard.getHalfMoves())
        .append("\s")
        .append(this.currentBoard.getFullMoves());
    return res.toString();
  }

  public enum Result {
    BLACK_WIN, WHITE_WIN, DRAW
  }

  public enum CheckStatus {
    BLACK_IN_CHECK, WHITE_IN_CHECK, BLACK_CHECKMATED, WHITE_CHECKMATED, NONE
  }

  public record Move(Position start, Position end, Piece piece, Piece capturedPiece) {

  }
}
