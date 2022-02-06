package de.profschmergmann.models;

public record Position(char column, int row) {
	public Position {
		if (column < 'a' || column > 'h') throw new IllegalArgumentException("Row out of bounds with value=" + column);
		if (row < 1 || row > 8) throw new IllegalArgumentException("Line out of bounds with value=" + row);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		var position = (Position) o;
		return column == position.column && row == position.row;
	}

	@Override
	public String toString() {
		return column + "" + row;
	}
}