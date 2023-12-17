package com.permanovd.gamesessionservice.domain;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.SortedSet;
import java.util.TreeSet;

@Data
@NoArgsConstructor
@JsonIncludeProperties({"size", "initialStonesNumberPerPit", "playerOnePits", "playerTwoPits", "moveLogs"})
public class BoardState {
    private int size;
    private int initialStonesNumberPerPit;
    private SortedSet<Pit> playerOnePits;
    private SortedSet<Pit> playerTwoPits;
    private SortedSet<MoveLogItem> moveLogs;

    public BoardState(int size, int initialStonesNumberPerPit) {
        this.size = size;
        this.initialStonesNumberPerPit = initialStonesNumberPerPit;
        playerOnePits = Pit.generateEmpty(size, initialStonesNumberPerPit);
        playerTwoPits = Pit.generateEmpty(size, initialStonesNumberPerPit);
        moveLogs = new TreeSet<>();
    }

    public static BoardState newDefaultSized() {
        return new BoardState(6, 6);
    }

    public boolean oneSideIsEmpty() {
        if (playerOnePits.stream().filter(Pit::isRegularPit).allMatch(Pit::isEmpty)) {
            return true;
        }
        return playerTwoPits.stream().filter(Pit::isRegularPit).allMatch(Pit::isEmpty);
    }

    public MoveResult lastMoveResult() {
        if (this.moveLogs.isEmpty()) return new MoveResult(this, new MoveLogItem(0, null, 0, Player.ONE, 0, false));
        return new MoveResult(this, this.moveLogs.last());
    }

    public boolean canMove(Player player) {
        if (this.moveLogs.isEmpty()) return player == Player.ONE;
        return this.moveLogs.last().nextPlayerToMove() == player;
    }

    public boolean gameIsOver() {
        if (this.moveLogs.isEmpty()) return false;
        return this.moveLogs.last().gameIsOver();
    }

    public int collectAllFromSide(Player player) {
        if (player == Player.ONE)
            return playerOnePits.stream().filter(Pit::isRegularPit).map(Pit::takeAllStones).reduce(Integer::sum).orElse(0);
        else
            return playerTwoPits.stream().filter(Pit::isRegularPit).map(Pit::takeAllStones).reduce(Integer::sum).orElse(0);
    }

    public int nextLogId() {
        if (this.moveLogs.isEmpty()) return 1;
        return this.moveLogs.last().id() + 1;
    }

    public int scoreFor(Player forPlayer) {
        return mancalaPitFor(forPlayer).stonesInside();
    }

    public Pit mancalaPitFor(Player forPlayer) {
        if (forPlayer == Player.ONE) {
            return playerOnePits.last();
        }
        return playerTwoPits.last();
    }

    public MoveLogItem logMove(Player player, int fromPit, Player nextToMove, int scoreAdded, boolean gameIsOver) {
        var log = new MoveLogItem(nextLogId(), player, fromPit, nextToMove, scoreAdded, gameIsOver);
        moveLogs.add(log);
        return log;
    }

    public MoveLogItem finishGame() {
        int p1Leftovers = collectAllFromSide(Player.ONE);
        mancalaPitFor(Player.ONE).addStones(p1Leftovers);
        int p2Leftovers = collectAllFromSide(Player.TWO);
        mancalaPitFor(Player.TWO).addStones(p2Leftovers);
        return logMove(null, 1, null, 0, true);
    }

}


