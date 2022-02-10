package de.profschmergmann.sad;

import de.profschmergmann.sad.pieces.Piece;

public record Command(Piece piece, int curX, int curY, int desX, int desY) {

}
