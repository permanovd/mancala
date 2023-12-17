package com.permanovd.gamesessionservice.domain;

public record MoveResult(BoardState newBoardState, MoveLogItem log) {
}
