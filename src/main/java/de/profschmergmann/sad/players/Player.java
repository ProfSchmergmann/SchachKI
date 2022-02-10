package de.profschmergmann.sad.players;

import de.profschmergmann.sad.pieces.Piece.PieceColor;

public record Player(PieceColor pieceColor, PlayerEngine engine) {

}
