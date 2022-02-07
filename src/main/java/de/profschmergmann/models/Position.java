package de.profschmergmann.models;

/**
 * Record position for storing a position like a1.
 */
public record Position(char file, int rank) {

	/**
	 * Constructor which checks for file and rank and throws an
	 * {@link IllegalArgumentException} if they are out of bounds.
	 *
	 * @param file the file which has to be between 'a' and 'h'	both inclusive
	 * @param rank the rank which has to be between 1 and 8 both inclusive
	 */
	public Position {
		if (file < 'a' || file > 'h') throw new IllegalArgumentException("File out of bounds with value=" + file);
		if (rank < 1 || rank > 8) throw new IllegalArgumentException("Rank out of bounds with value=" + rank);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || this.getClass() != o.getClass()) return false;
		var position = (Position) o;
		return this.file == position.file &&
				this.rank == position.rank;
	}

	@Override
	public String toString() {
		return this.file + "" + this.rank;
	}
}
