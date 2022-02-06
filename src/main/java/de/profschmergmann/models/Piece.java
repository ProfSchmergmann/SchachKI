package de.profschmergmann.models;

public record Piece(PieceEnum piece, Team team) {

	public String toString() {
		return this.piece.identifier + (team.equals(Team.WHITE) ? "0" : "1");
	}

	public enum Team {
		WHITE, BLACK
	}

	public enum PieceEnum {
		BISHOP("B"), KING("K"), KNIGHT("N"), PAWN("P"), QUEEN("Q"), ROOK("R");

		final String identifier;

		PieceEnum(String identifier) {
			this.identifier = identifier;
		}
	}
}
