package com.permanovd.gamesessionservice.domain.exceptions;

public class PitOutOfBoardRangeException extends MoveIsInvalidException {
    public PitOutOfBoardRangeException() {
    }

    public PitOutOfBoardRangeException(String message) {
        super(message);
    }
}
