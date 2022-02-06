package de.profschmergmann.models;

import java.util.ArrayList;
import java.util.List;

public class Game {

	private final List<Board> boards;
	private final Piece.Team currentTeam;

	public Game() {
		this.boards = new ArrayList<>();
		this.boards.add(new Board());
		this.currentTeam = Piece.Team.WHITE;
	}

	public boolean move(Position from, Position to, Piece.Team currentTeam) {
		var newBoard = boards.get(boards.size() - 1).move(from, to, currentTeam);
		if (newBoard != null) {
			this.boards.add(newBoard);
			currentTeam = currentTeam.equals(Piece.Team.WHITE) ? Piece.Team.BLACK : Piece.Team.WHITE;
			return true;
		}
		return false;
	}

}
