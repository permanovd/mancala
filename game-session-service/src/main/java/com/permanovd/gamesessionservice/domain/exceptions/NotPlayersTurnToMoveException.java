package com.permanovd.gamesessionservice.domain.exceptions;

public class NotPlayersTurnToMoveException extends MoveIsInvalidException {
    public NotPlayersTurnToMoveException(String message) {
        super(message);
    }
}
