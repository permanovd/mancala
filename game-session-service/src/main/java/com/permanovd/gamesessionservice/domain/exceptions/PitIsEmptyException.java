package com.permanovd.gamesessionservice.domain.exceptions;

public class PitIsEmptyException extends MoveIsInvalidException {
    public PitIsEmptyException(String message) {
        super(message);
    }
}
