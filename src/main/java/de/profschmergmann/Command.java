package de.profschmergmann;

import de.profschmergmann.pieces.Piece;

public record Command(Piece piece, int curX, int curY, int desX, int desY) {

}
