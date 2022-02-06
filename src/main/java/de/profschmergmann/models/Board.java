package de.profschmergmann.models;

import java.util.HashMap;
import java.util.HashSet;

import static de.profschmergmann.models.Piece.PieceEnum.*;

public class Board {

	private final HashMap<Position, Piece> positions;

	public Board() {
		this.positions = new HashMap<>();
		this.initBoard();
	}

	private void initBoard() {
		var white = Piece.Team.WHITE;
		var black = Piece.Team.BLACK;

		// Pawns
		for (var c = 'a'; c <= 'h'; c++) {
			this.positions.put(new Position(c, 2), new Piece(Piece.PieceEnum.PAWN, white));
			this.positions.put(new Position(c, 7), new Piece(Piece.PieceEnum.PAWN, black));
		}

		// Rooks
		var rook = Piece.PieceEnum.ROOK;
		this.positions.put(new Position('a', 1), new Piece(rook, white));
		this.positions.put(new Position('h', 1), new Piece(rook, white));
		this.positions.put(new Position('a', 8), new Piece(rook, black));
		this.positions.put(new Position('h', 8), new Piece(rook, black));

		// Knights
		var knight = KNIGHT;
		this.positions.put(new Position('b', 1), new Piece(knight, white));
		this.positions.put(new Position('g', 1), new Piece(knight, white));
		this.positions.put(new Position('b', 8), new Piece(knight, black));
		this.positions.put(new Position('g', 8), new Piece(knight, black));

		// Bishops
		var bishop = BISHOP;
		this.positions.put(new Position('c', 1), new Piece(bishop, white));
		this.positions.put(new Position('f', 1), new Piece(bishop, white));
		this.positions.put(new Position('c', 8), new Piece(bishop, black));
		this.positions.put(new Position('f', 8), new Piece(bishop, black));

		// Queens
		this.positions.put(new Position('d', 1), new Piece(Piece.PieceEnum.QUEEN, white));
		this.positions.put(new Position('d', 8), new Piece(Piece.PieceEnum.QUEEN, black));

		// Kings
		this.positions.put(new Position('e', 1), new Piece(KING, white));
		this.positions.put(new Position('e', 8), new Piece(KING, black));
	}

	public String toString() {
		var res = new StringBuilder();
		res.append("0 equals white and 1 equals black.\n").append("  ");
		for (var c = 'a'; c <= 'h'; c++) {
			res.append(c).append("  ");
		}
		res.append("\n");
		for (var i = 8; i >= 1; i--) {
			res.append(i).append(" ");
			for (var c = 'a'; c <= 'h'; c++) {
				if (this.positions.containsKey(new Position(c, i))) {
					res.append(this.positions.get(new Position(c, i)));
				} else {
					res.append("##");
				}
				res.append(" ");
				if (c == 'h') res.append(i);
			}
			res.append("\n");
		}
		res.append("  ");
		for (var c = 'a'; c <= 'h'; c++) {
			res.append(c).append("  ");
		}
		res.append("\n");
		return res.toString();
	}

	public Board move(Position from, Position to, Piece.Team currentTeam) throws IllegalArgumentException {
		if (!this.positions.containsKey(from)) throw new IllegalArgumentException("Move not possible from: " + from);
		if (this.positions.containsKey(to) && this.positions.get(to).team() == currentTeam)
			throw new IllegalArgumentException("Cannot attack same team from: " + from + " to: " + to);
		var pieceToMove = this.positions.get(from);
		this.positions.remove(from);
		this.positions.put(to, pieceToMove);
		return this;
	}

	public HashSet<Move> getAvailableMoves(Piece.Team team) {
		var set = new HashSet<Move>();
		for (var entry : this.positions.entrySet()) {
			var fullPiece = entry.getValue();
			if (!fullPiece.team().equals(team)) continue;
			var currentPos = entry.getKey();
			var piece = entry.getValue().piece();
			if (piece == BISHOP || piece == QUEEN) {
				if (currentPos.column() - 1 >= 'a' && currentPos.row() + 1 <= 8) {
					var c = (char) (currentPos.column() - 1);
					var i = currentPos.row() + 1;
					while (c >= 1 && i <= 8) {
						if (addMoveToSetIfPossible(team, set, currentPos, piece, c, i)) break;
						c--;
						i++;
					}
				}
				if (currentPos.column() + 1 <= 'h' && currentPos.row() + 1 <= 8) {
					var c = (char) (currentPos.column() + 1);
					var i = currentPos.row() + 1;
					while (c <= 'h' && i <= 8) {
						if (addMoveToSetIfPossible(team, set, currentPos, piece, c, i)) break;
						c++;
						i++;
					}
				}
				if (currentPos.column() + 1 <= 'h' && currentPos.row() - 1 >= 1) {
					var c = (char) (currentPos.column() + 1);
					var i = currentPos.row() - 1;
					while (c <= 'h' && i <= 8) {
						if (addMoveToSetIfPossible(team, set, currentPos, piece, c, i)) break;
						c++;
						i--;
					}
				}
				if (currentPos.column() - 1 >= 'a' && currentPos.row() - 1 >= 1) {
					var c = (char) (currentPos.column() - 1);
					var i = currentPos.row() - 1;
					while (c <= 'h' && i <= 8) {
						if (addMoveToSetIfPossible(team, set, currentPos, piece, c, i)) break;
						c--;
						i--;
					}
				}
			}
			if (piece == KING) {
				for (var c = 'a'; c <= 'h'; c++) {
					for (var i = 1; i <= 8; i++) {
						if ((((currentPos.column() - 1) == c) && ((currentPos.row() + 1) == i)) ||
								((currentPos.column() == c) && ((currentPos.row() + 1) == i)) ||
								(((currentPos.column() + 1) == c) && ((currentPos.row() + 1) == i)) ||
								(((currentPos.column() + 1) == c) && (currentPos.row() == i)) ||
								(((currentPos.column() - 1) == c) && ((currentPos.row() - 1) == i)) ||
								((currentPos.column() == c) && (currentPos.row() == (i - 1))) ||
								(((currentPos.column() + 1) == c) && ((currentPos.row() - 1) == i)) ||
								(((currentPos.column() - 1) == c) && (currentPos.row() == i))) {
							addMoveToSetIfPossible(team, set, currentPos, piece, c, i);
						}
					}
				}
			}
			if (piece == KNIGHT) {
				for (var c = 'a'; c <= 'h'; c++) {
					for (var i = 1; i <= 8; i++) {
						if (currentPos.column() - 2 == c && currentPos.row() + 1 == i ||
								currentPos.column() - 1 == c && currentPos.row() + 2 == i ||
								currentPos.column() + 1 == c && currentPos.row() + 2 == i ||
								currentPos.column() + 2 == c && currentPos.row() + 1 == i ||
								currentPos.column() + 2 == c && currentPos.row() - 1 == i ||
								currentPos.column() + 1 == c && currentPos.row() - 2 == i ||
								currentPos.column() - 1 == c && currentPos.row() - 2 == i ||
								currentPos.column() - 2 == c && currentPos.row() - 1 == i
						) {
							addMoveToSetIfPossible(team, set, currentPos, piece, c, i);
						}
					}
				}
			}
			if (piece == PAWN) {
				if (currentPos.row() + 1 <= 8) {
					var top = new Position(currentPos.column(), currentPos.row() + 1);
					if (!this.positions.containsKey(top)) {
						set.add(new Move(currentPos, top, piece, false));
					}
				}
				if (currentPos.column() + 1 <= 'h' && currentPos.row() + 1 <= 8) {
					var topLeft = new Position((char) (currentPos.column() + 1), currentPos.row() + 1);
					if (this.positions.containsKey(topLeft)) {
						set.add(new Move(currentPos, topLeft, piece, true));
					}
				}
			}
			if (piece == ROOK || piece == QUEEN) {
				if ((currentPos.column() - 1) >= 'a') {
					var c = (char) (currentPos.column() - 1);
					while (c >= 1) {
						if (addMoveToSetIfPossible(team, set, currentPos, piece, c, currentPos.row())) break;
						c--;
					}
				}
				if (currentPos.row() + 1 <= 8) {
					var i = currentPos.row() + 1;
					while (i >= 1) {
						if (addMoveToSetIfPossible(team, set, currentPos, piece, currentPos.column(), i)) break;
						i--;
					}
				}
				if ((currentPos.column() + 1) <= 'h') {
					var c = (char) (currentPos.column() + 1);
					while (c >= 1) {
						if (addMoveToSetIfPossible(team, set, currentPos, piece, c, currentPos.row())) break;
						c--;
					}
				}
				if (currentPos.row() - 1 >= 1) {
					var i = currentPos.row() - 1;
					while (i >= 1) {
						if (addMoveToSetIfPossible(team, set, currentPos, piece, currentPos.column(), i)) break;
						i--;
					}
				}
			}
		}
		return set;
	}

	private boolean addMoveToSetIfPossible(Piece.Team team, HashSet<Move> set, Position currentPos,
	                                       Piece.PieceEnum piece, char c, int i) {
		var neighbourPos = new Position(c, i);
		if (this.positions.containsKey(neighbourPos)) {
			if (this.positions.get(neighbourPos).team() != team) {
				set.add(new Move(currentPos, neighbourPos, piece, true));
			}
			return true;
		}
		set.add(new Move(currentPos, neighbourPos, piece, false));
		return false;
	}
}
