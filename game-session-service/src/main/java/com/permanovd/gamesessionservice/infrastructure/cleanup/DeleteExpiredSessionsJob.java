package com.permanovd.gamesessionservice.infrastructure.cleanup;

import com.permanovd.gamesessionservice.domain.BoardState;
import com.permanovd.gamesessionservice.domain.GameSession;
import com.permanovd.gamesessionservice.domain.GameSessionRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class DeleteExpiredSessionsJob {

    private final GameSessionRepository sessionRepository;

    private final Logger logger = LoggerFactory.getLogger(DeleteExpiredSessionsJob.class);

    DeleteExpiredSessionsJob(GameSessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Transactional
//    @Scheduled(cron = "0/10 * * ? * *")
    public void cleanUp() {
        // TODO Implement.
    }
}
