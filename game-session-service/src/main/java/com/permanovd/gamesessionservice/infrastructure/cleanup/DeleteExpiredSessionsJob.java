package com.permanovd.gamesessionservice.infrastructure.cleanup;

import com.permanovd.gamesessionservice.domain.GameSession;
import com.permanovd.gamesessionservice.domain.GameSessionRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Component
public class DeleteExpiredSessionsJob {

    private final GameSessionRepository sessionRepository;

    private final Logger logger = LoggerFactory.getLogger(DeleteExpiredSessionsJob.class);

    DeleteExpiredSessionsJob(GameSessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void cleanUp() {
        LocalDateTime dateTime = LocalDateTime.now().minusMonths(1);
        logger.info("Cleaning up expired sessions, ended before {}", dateTime);
        sessionRepository.delete((Root<GameSession> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> cb.lessThan(root.get("endedAt"), dateTime));
    }
}
