package com.permanovd.gamesessionservice.domain.exceptions;

public class SessionNotFoundException extends Exception {
    public SessionNotFoundException() {
    }

    public SessionNotFoundException(String message) {
        super(message);
    }
}
