package com.permanovd.gamesessionservice.domain;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GameSessionRepository extends CrudRepository<GameSession, UUID>, JpaSpecificationExecutor<GameSession> {
}
