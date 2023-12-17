package com.permanovd.gamesessionservice.controller;

import com.permanovd.gamesessionservice.application.MancalaGameSessionService;
import com.permanovd.gamesessionservice.controller.dto.GameSessionDto;
import com.permanovd.gamesessionservice.domain.GameSession;
import com.permanovd.gamesessionservice.domain.MoveResult;
import com.permanovd.gamesessionservice.domain.Player;
import com.permanovd.gamesessionservice.domain.exceptions.MoveIsInvalidException;
import com.permanovd.gamesessionservice.domain.exceptions.SessionNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game-session/mancala")
@CrossOrigin
public class MancalaGameSessionController {

    private final MancalaGameSessionService gameSessionService;

    public MancalaGameSessionController(MancalaGameSessionService gameSessionService) {
        this.gameSessionService = gameSessionService;
    }

    @PostMapping("/create")
    public GameSessionDto createNewGame() {
        return GameSessionDto.toDto(gameSessionService.startGameSession());
    }

    @PostMapping("/{id}/move")
    public GameSessionDto makeMove(@PathVariable String id, @RequestBody MoveRequest moveRequest) throws MoveIsInvalidException, SessionNotFoundException {
        return GameSessionDto.toDto(gameSessionService.makeMove(id, Player.fromNum(moveRequest.player), moveRequest.fromPit));
    }

    @GetMapping("/{id}")
    public GameSessionDto getSession(@PathVariable String id) throws SessionNotFoundException {
        return GameSessionDto.toDto(gameSessionService.getGameSession(id));
    }
}
