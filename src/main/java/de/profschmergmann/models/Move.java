package de.profschmergmann.models;

import java.util.Objects;

public record Move(Position from, Position to, Piece.PieceEnum piece, boolean canAttack) {
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || this.getClass() != o.getClass()) return false;
		Move move = (Move) o;
		return this.canAttack == move.canAttack &&
				Objects.equals(this.from, move.from) &&
				Objects.equals(this.to, move.to) &&
				this.piece == move.piece;
	}

	@Override
	public String toString() {
		return (this.canAttack ? "Can attack " : "Can move ")
				+ this.from + " -> " + this.to + " with " + this.piece.identifier;
	}
}
