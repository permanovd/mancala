package com.permanovd.gamesessionservice.domain;

import com.permanovd.gamesessionservice.domain.exceptions.*;
import com.permanovd.gamesessionservice.infrastructure.PitWithOwner;
import org.springframework.stereotype.Service;

@Service
public class MancalaGameService {

    public BoardState createNewGame() {
        return BoardState.newDefaultSized();
    }

    public MoveResult move(BoardState boardState, Player player, int pitNumber) throws MoveIsInvalidException {
        if (boardState.gameIsOver()) throw new GameIsOverException();
        if (!boardState.canMove(player))
            throw new NotPlayersTurnToMoveException("Player " + player + " cannot make a move. Turn of another player");

        var boardWalker = getWalker(boardState, player, pitNumber);
        PitWithOwner startPit = boardWalker.next();
        int stonesFromPit = startPit.pit().takeAllStones();
        if (stonesFromPit == 0) throw new PitIsEmptyException("Pit " + pitNumber + " is empty");

        int scoreAdded = 0;
        var nextPlayerToMove = player.otherOne();
        while (stonesFromPit > 0) {
            var currentPit = boardWalker.next();
            if (currentPit.isAnotherPlayersMancala(player)) continue;

            currentPit.pit().addStone();
            stonesFromPit--;

            if (currentPit.isCurrentPlayersMancala(player)) scoreAdded++;
            boolean isLastStep = stonesFromPit == 0;
            if (isLastStep && currentPit.isCurrentPlayersMancala(player)) nextPlayerToMove = player;
            if (isLastStep && currentPit.isCurrentPlayersRegularPit(player) && currentPit.pit().stonesInside() == 1) {
                scoreAdded = claimStonesFromOppositePit(boardState, player, boardWalker, currentPit, scoreAdded);
            }
        }

        MoveLogItem log = boardState.logMove(player, pitNumber, nextPlayerToMove, scoreAdded, false);
        // Game is over.
        if (boardState.oneSideIsEmpty()) return new MoveResult(boardState, boardState.finishGame());

        return new MoveResult(boardState, log);
    }

    private BoardWalker getWalker(BoardState boardState, Player player, int pitNumber) throws PitOutOfBoardRangeException {
        return new BoardWalker(boardState.getPlayerOnePits(), boardState.getPlayerTwoPits(), boardState.getSize(), player, pitNumber);
    }

    private int claimStonesFromOppositePit(BoardState boardState, Player player, BoardWalker boardWalker, PitWithOwner currentPit, int scoreAdded) {
        int stonesFromOppositePit = boardWalker.fromOppositeSide().pit().takeAllStones();
        int stonesFromCurrentPit = currentPit.pit().takeAllStones();
        boardState.mancalaPitFor(player).addStones(stonesFromOppositePit);
        boardState.mancalaPitFor(player).addStones(stonesFromCurrentPit);
        scoreAdded += stonesFromOppositePit + stonesFromCurrentPit;
        return scoreAdded;
    }
}
