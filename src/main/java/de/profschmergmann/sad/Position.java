package de.profschmergmann.sad;


import java.util.logging.Logger;

/**
 * Record position for storing a position like a1.
 */
public record Position(char file, int rank) implements Comparable<Position> {

  private static final Logger LOGGER = Logger.getLogger(Position.class.getName());

  /**
   * Constructor which checks for file and rank and logs if they are out of bounds.
   *
   * @param file the file which has to be between 'a' and 'h'	both inclusive
   * @param rank the rank which has to be between 1 and 8 both inclusive
   */
  public Position {
    if (file < 'a' || file > 'h') {
      LOGGER.warning("File out of bounds with value=" + file + " and rank=" + rank);
    }
    if (rank < 1 || rank > 8) {
      LOGGER.warning("Rank out of bounds with value=" + rank + " and file=" + file);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    var position = (Position) o;
    return this.file == position.file &&
        this.rank == position.rank;
  }

  @Override
  public String toString() {
    return this.file + "" + this.rank;
  }

  @Override
  public int compareTo(Position o) {
    if (this.file != o.file) {
      return this.file - o.file;
    }
    if (this.rank != o.rank) {
      return this.rank - o.rank;
    }
    return 0;
  }
}
