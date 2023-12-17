package com.permanovd.gamesessionservice.domain;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Player {
    ONE, TWO;

    @JsonValue
    public int toValue() {
        if (this == Player.ONE) return 1;
        if (this == Player.TWO) return 2;
        throw new RuntimeException();
    }

    public Player otherOne() {
        if (this == Player.TWO) return Player.ONE;
        else return Player.TWO;
    }

    public static Player fromNum(int playerNum) {
        if (playerNum == 1) return Player.ONE;
        if (playerNum == 2) return Player.TWO;
        throw new RuntimeException("Invalid player number " + playerNum);
    }
}
