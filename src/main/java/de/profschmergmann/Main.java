package de.profschmergmann;

import de.profschmergmann.models.Board;
import de.profschmergmann.models.Piece;
import de.profschmergmann.models.Position;

public class Main {
	public static void main(String[] args) {
		var b = new Board();
		System.out.println(b);
		b.getAvailableMoves(Piece.Team.WHITE).forEach(System.out::println);
		b = b.move(new Position('h', 2), new Position('h', 3), Piece.Team.WHITE);
		System.out.println(b);
	}
}
