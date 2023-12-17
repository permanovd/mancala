package com.permanovd.gamesessionservice.controller;

import com.permanovd.gamesessionservice.domain.exceptions.MoveIsInvalidException;
import com.permanovd.gamesessionservice.domain.exceptions.NotPlayersTurnToMoveException;
import com.permanovd.gamesessionservice.domain.exceptions.PitIsEmptyException;
import com.permanovd.gamesessionservice.domain.exceptions.SessionNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Component
@ControllerAdvice(annotations = RestController.class)
public class ControllerExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler(SessionNotFoundException.class)
    public ResponseEntity<Map<String, ?>> sessionNotFound(SessionNotFoundException exception) {
        return handle(exception, "Session is not found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MoveIsInvalidException.class)
    public ResponseEntity<Map<String, ?>> invalidMove(MoveIsInvalidException exception) {
        return handle(exception, exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, ?>> undefinedError(SessionNotFoundException exception) {
        return handle(exception, "Server error", HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<Map<String, ?>> handle(Exception exception, String formattedErrorResponse, HttpStatus status) {
        if (status == INTERNAL_SERVER_ERROR) {
            log.error("Unexpected exception returned from a controller", exception);
        } else {
            log.warn("Controller responded with exception and status " + status, exception);
        }
        var result = new HashMap<String, String>();
        result.put("message", formattedErrorResponse);
        return new ResponseEntity<>(result, status);
    }
}
