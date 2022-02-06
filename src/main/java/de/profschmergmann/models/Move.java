package de.profschmergmann.models;

import java.util.Objects;

public record Move(Position from, Position to, Piece.PieceEnum piece, boolean canAttack) {
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Move move = (Move) o;
		return canAttack == move.canAttack && Objects.equals(from, move.from) && Objects.equals(to, move.to) && piece == move.piece;
	}

	@Override
	public String toString() {
		return (canAttack ? "Can attack " : "Can move ")
				+ from + " -> " + to + " with " + piece;
	}
}
