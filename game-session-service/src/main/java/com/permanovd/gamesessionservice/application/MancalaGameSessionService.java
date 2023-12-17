package com.permanovd.gamesessionservice.application;


import com.permanovd.gamesessionservice.domain.*;
import com.permanovd.gamesessionservice.domain.exceptions.MoveIsInvalidException;
import com.permanovd.gamesessionservice.domain.exceptions.SessionNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MancalaGameSessionService {
    private final MancalaGameService mancalaGameService;
    private final GameSessionRepository gameSessionRepository;
    private final Logger logger = LoggerFactory.getLogger(MancalaGameSessionService.class);

    public MancalaGameSessionService(MancalaGameService mancalaGameService, GameSessionRepository gameSessionRepository) {
        this.mancalaGameService = mancalaGameService;
        this.gameSessionRepository = gameSessionRepository;
    }

    @Transactional
    public GameSession startGameSession() {
        logger.info("Starting new game session");
        return gameSessionRepository.save(new GameSession(mancalaGameService.createNewGame()));
    }

    @Transactional
    public GameSession makeMove(String id, Player player, int fromPit) throws SessionNotFoundException, MoveIsInvalidException {
        logger.info("Making move for session {} player {} from pit {}", id, player, fromPit);
        GameSession gameSession = getGameSession(id);
        MoveResult moveResult = mancalaGameService.move(gameSession.getBoardState(), player, fromPit);
        if (moveResult.log().gameIsOver()) {
            gameSession.endSession();
            logger.info("Game session {} is over", id);
        }
        gameSessionRepository.save(gameSession);
        return gameSession;
    }

    @Transactional
    public GameSession getGameSession(String id) throws SessionNotFoundException {
        logger.info("Getting game session {}", id);
        return gameSessionRepository.findById(UUID.fromString(id))
                .orElseThrow(SessionNotFoundException::new);
    }
}
