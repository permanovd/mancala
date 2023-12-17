package com.permanovd.gamesessionservice.domain;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;

public record MoveLogItem(int id, Player player, int fromPit, Player nextPlayerToMove,
                          int scoreAdded, boolean gameIsOver) implements Comparable<MoveLogItem> {
    @Override
    public int compareTo(MoveLogItem o) {
        return Integer.compare(id, o.id);
    }
}
