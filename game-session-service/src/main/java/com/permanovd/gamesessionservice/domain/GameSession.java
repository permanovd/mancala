package com.permanovd.gamesessionservice.domain;

import com.permanovd.gamesessionservice.infrastructure.jpa.JpaConverterBoardStateToJson;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "game_session")
@Getter
@NoArgsConstructor
public final class GameSession {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "started_at")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime startedAt = LocalDateTime.now();
    @Column(name = "ended_at")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime endedAt;
    @Column(name = "board_state", columnDefinition = "json")
    @Convert(converter = JpaConverterBoardStateToJson.class)
    private BoardState boardState;

    public GameSession(BoardState boardState) {
        this.boardState = boardState;
    }

    public void endSession() {
        endedAt = LocalDateTime.now();
    }
}
