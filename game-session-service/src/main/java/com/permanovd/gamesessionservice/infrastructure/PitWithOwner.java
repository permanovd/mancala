package com.permanovd.gamesessionservice.infrastructure;

import com.permanovd.gamesessionservice.domain.Pit;
import com.permanovd.gamesessionservice.domain.Player;

public record PitWithOwner(Pit pit, Player owner) {
    public boolean isAnotherPlayersMancala(Player player) {
        return player != owner && pit.isMancalaPit();
    }
    public boolean isCurrentPlayersMancala(Player player) {
        return player == owner && pit.isMancalaPit();
    }
    public boolean isCurrentPlayersRegularPit(Player player) {
        return player == owner && !pit.isMancalaPit();
    }
}
