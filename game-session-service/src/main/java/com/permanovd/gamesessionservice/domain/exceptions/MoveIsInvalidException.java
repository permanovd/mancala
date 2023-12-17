package com.permanovd.gamesessionservice.domain.exceptions;

public class MoveIsInvalidException extends Exception {
    public MoveIsInvalidException() {
    }

    public MoveIsInvalidException(String message) {
        super(message);
    }
}
