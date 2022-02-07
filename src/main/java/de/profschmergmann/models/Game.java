package de.profschmergmann.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Class game for holding multiple games as an ArrayList.
 */
public class Game {

	private final List<Board> boards = new ArrayList<>();
	private Piece.Team currentTeam;
	private boolean whiteCanCastleKingSide;
	private boolean whiteCanCastleQueenSide;
	private boolean blackCanCastleKingSide;
	private boolean blackCanCastleQueenSide;
	private Position enPassant;
	private int halfMoves;
	private int fullMoves;

	/**
	 * Constructor for a game where white starts.
	 */
	public Game() {
		this.boards.add(new Board());
		this.currentTeam = Piece.Team.WHITE;
		this.whiteCanCastleKingSide = true;
		this.whiteCanCastleQueenSide = true;
		this.blackCanCastleKingSide = true;
		this.blackCanCastleQueenSide = true;
		this.enPassant = null;
		this.halfMoves = 0;
		this.fullMoves = 1;
	}

	/**
	 * Constructor for a game with a given FEN-record.
	 * If the FEN-record is the starting record you can also use the constructor without any parameters.
	 *
	 * @param FENRecord a String parsed as
	 *                  <a href="https://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation">FEN-Notation</a>
	 */
	public Game(String FENRecord) {
		var splitRecord = FENRecord.split("\s");
		this.boards.add(new Board(splitRecord[0]));
		this.currentTeam = splitRecord[1].equals("w") ? Piece.Team.WHITE : Piece.Team.BLACK;
		if (splitRecord[2].equals("-")) {
			this.whiteCanCastleKingSide = false;
			this.whiteCanCastleQueenSide = false;
			this.blackCanCastleKingSide = false;
			this.blackCanCastleQueenSide = false;
		} else {
			if (splitRecord[2].contains("K")) this.whiteCanCastleKingSide = true;
			if (splitRecord[2].contains("Q")) this.whiteCanCastleQueenSide = true;
			if (splitRecord[2].contains("k")) this.blackCanCastleKingSide = true;
			if (splitRecord[2].contains("q")) this.blackCanCastleQueenSide = true;
		}
		if (!splitRecord[3].equals("-")) {
			this.enPassant = new Position(splitRecord[3].charAt(0),
					Integer.parseInt(String.valueOf(splitRecord[3].charAt(1)))
			);
		}
		this.halfMoves = Integer.parseInt(String.valueOf(splitRecord[4].charAt(0)));
		this.fullMoves = Integer.parseInt(String.valueOf(splitRecord[5].charAt(0)));
	}

	public Board getCurrentBoard() {
		return this.boards.get(this.boards.size() - 1);
	}

	public Piece.Team getCurrentTeam() {
		return this.currentTeam;
	}

	/**
	 * Method for performing a move inside the current game.
	 * TODO: the halfmoves and fullmoves variables are not updated inside this method.
	 *
	 * @param from the {@link Position} from where the move should be performed
	 * @param to   the {@link Position} to where the move should be performed
	 * @return true if the move worked, else false
	 */
	public boolean move(Position from, Position to) {
		var newBoard = this.boards.get(this.boards.size() - 1).move(from, to, this.currentTeam);
		if (newBoard != null) {
			this.boards.add(newBoard);
			this.currentTeam = this.currentTeam.equals(Piece.Team.WHITE) ? Piece.Team.BLACK : Piece.Team.WHITE;
			this.fullMoves++;
			// TODO!
			this.halfMoves++;
			return true;
		}
		return false;
	}

	public HashSet<Move> getAvailableMoves() {
		return this.boards.get(this.boards.size() - 1).getAvailableMoves(this.currentTeam);
	}

	/**
	 * Method for returning the current game as a
	 * <a href="https://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation">FEN-Notation</a>.
	 *
	 * @return the current game as FEN Notation
	 */
	public String getCurrentGameAsFENRecord() {
		var board = new Piece[8][8];
		this.boards.get(this.boards.size() - 1)
		           .getPositions()
		           .forEach((position, piece) -> {
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
					res.append(board[i][j].piece().identifier);
				} else {
					space++;
				}
			}
			if (space > 0) res.append(space);
			res.append("/");
		}
		res.deleteCharAt(res.length() - 1)
		   .append("\s")
		   .append(this.currentTeam.identifier)
		   .append("\s");
		var castleTmp = "";
		if (this.whiteCanCastleKingSide) castleTmp += "K";
		if (this.whiteCanCastleQueenSide) castleTmp += "Q";
		if (this.blackCanCastleKingSide) castleTmp += "k";
		if (this.blackCanCastleQueenSide) castleTmp += "q";
		if (!castleTmp.isEmpty()) res.append(castleTmp);
		res.append("\s")
		   .append(this.enPassant != null ? this.enPassant : "-")
		   .append("\s")
		   .append(this.halfMoves)
		   .append("\s")
		   .append(this.fullMoves);
		return res.toString();
	}
}
