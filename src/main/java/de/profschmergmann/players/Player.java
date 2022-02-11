package de.profschmergmann.players;

import de.profschmergmann.pieces.Piece.PieceColor;

public record Player(PieceColor pieceColor, PlayerEngine engine) {

}
