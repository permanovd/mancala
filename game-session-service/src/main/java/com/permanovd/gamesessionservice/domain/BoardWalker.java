package com.permanovd.gamesessionservice.domain;

import com.permanovd.gamesessionservice.domain.exceptions.PitOutOfBoardRangeException;
import com.permanovd.gamesessionservice.infrastructure.PitWithOwner;

import java.util.Iterator;
import java.util.SortedSet;

public class BoardWalker implements Iterator<PitWithOwner> {
    private final SortedSet<Pit> playerOnePits;
    private final SortedSet<Pit> playerTwoPits;
    private final int boardSize;
    private int currentPit;
    private Player currentBoardSide;
    private Iterator<Pit> currentBoardIterator;

    public BoardWalker(SortedSet<Pit> playerOnePits, SortedSet<Pit> playerTwoPits, int boardSize, Player fromPlayer, int fromPit) throws PitOutOfBoardRangeException {
        if (fromPit < 1 || fromPit > boardSize)
            throw new PitOutOfBoardRangeException("Invalid number of pit " + fromPit + ". Has to be between 1 and " + boardSize);
        this.playerOnePits = playerOnePits;
        this.playerTwoPits = playerTwoPits;
        this.boardSize = boardSize;
        this.currentPit = fromPit;
        currentBoardSide = fromPlayer;
        currentBoardIterator = getCurrentBoard().iterator();
        // Move iterator to needed position.
        for (int i = 1; i < fromPit; i++) {
            currentBoardIterator.next();
        }
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public PitWithOwner next() {
        int realBoardSize = boardSize + 1;
        boolean crossedToOtherPlayer = currentPit > realBoardSize;
        if (crossedToOtherPlayer) {
            currentBoardSide = currentBoardSide.otherOne();
            currentBoardIterator = getCurrentBoard().iterator();
            currentPit = 1;
        }
        Pit result = currentBoardIterator.next();
        currentPit++;
        return new PitWithOwner(result, currentBoardSide);
    }

    public PitWithOwner fromOppositeSide() {
        // currentPit is already shifted to the next position.
        int pitFromOtherSide = (boardSize + 1) - (currentPit - 1);
        Iterator<Pit> iterator;
        if (currentBoardSide == Player.ONE) iterator = playerTwoPits.iterator();
        else iterator = playerOnePits.iterator();
        for (int i = 1; i < pitFromOtherSide; i++) {
            iterator.next();
        }
        return new PitWithOwner(iterator.next(), currentBoardSide.otherOne());
    }

    private SortedSet<Pit> getCurrentBoard() {
        if (currentBoardSide == Player.ONE) {
            return playerOnePits;
        }
        return playerTwoPits;
    }
}
