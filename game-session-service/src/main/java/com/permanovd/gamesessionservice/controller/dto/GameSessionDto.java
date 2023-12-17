package com.permanovd.gamesessionservice.controller.dto;

import com.permanovd.gamesessionservice.domain.BoardState;
import com.permanovd.gamesessionservice.domain.GameSession;
import com.permanovd.gamesessionservice.domain.Player;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class GameSessionDto {
    private String id;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private BoardState boardState;
    private Player nextPlayerToMove;
    private boolean gameIsOver;
    private int playerOneScore;
    private int playerTwoScore;
    private Player winner;

    public static GameSessionDto toDto(GameSession gameSession) {
        boolean gamedIsOver = gameSession.getBoardState().lastMoveResult().log().gameIsOver();
        int playerOneScore = gameSession.getBoardState().scoreFor(Player.ONE);
        int playerTwoScore = gameSession.getBoardState().scoreFor(Player.TWO);
        Player winner = null;
        if (gamedIsOver) {
            winner = playerOneScore > playerTwoScore ? Player.ONE : Player.TWO;
        }
        return new GameSessionDto(
                gameSession.getId().toString(),
                gameSession.getStartedAt(),
                gameSession.getEndedAt(),
                gameSession.getBoardState(),
                gameSession.getBoardState().lastMoveResult().log().nextPlayerToMove(),
                gamedIsOver,
                playerOneScore,
                playerTwoScore,
                winner
        );
    }
}
