package de.profschmergmann.models;

/**
 * Dataclass piece for holding a single chess peace with its team.
 * This record also has a team and a piece enum for returning SAN values.
 */
public record Piece(PieceEnum piece) {

	/**
	 * ToString.
	 *
	 * @return the identifier of this piece.
	 */
	public String toString() {
		return this.piece.identifier;
	}

	/**
	 * If the piece belongs to white.
	 *
	 * @return white, if the identifier is upper case, black otherwise
	 */
	private boolean isWhite() {
		return Character.isUpperCase(this.piece.identifier.toCharArray()[0]);
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
	 * Team enum with identifiers.
	 */
	public enum Team {
		WHITE("w"), BLACK("b");

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
