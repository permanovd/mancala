package com.permanovd.gamesessionservice.domain;

import com.permanovd.gamesessionservice.domain.exceptions.GameIsOverException;
import com.permanovd.gamesessionservice.domain.exceptions.MoveIsInvalidException;
import com.permanovd.gamesessionservice.domain.exceptions.NotPlayersTurnToMoveException;
import com.permanovd.gamesessionservice.domain.exceptions.PitIsEmptyException;
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

        var boardWalker = new BoardWalker(boardState.getPlayerOnePits(), boardState.getPlayerTwoPits(), boardState.getSize(), player, pitNumber);
        PitWithOwner startPit = boardWalker.next();
        int stonesFromPit = startPit.pit().takeAllStones();
        if (stonesFromPit == 0) throw new PitIsEmptyException("Pit " + pitNumber + " is empty");

        int scoreAdded = 0;
        var nextPlayerToMove = player.otherOne();
        while (stonesFromPit > 0) {
            PitWithOwner pitWithOwner = boardWalker.next();
            if (!pitWithOwner.isAnotherPlayersMancala(player)) {
                pitWithOwner.pit().addStone();
                stonesFromPit--;
            }
            if (pitWithOwner.isCurrentPlayersMancala(player)) scoreAdded++;
            boolean isLastStep = stonesFromPit == 0;
            if (isLastStep && pitWithOwner.isCurrentPlayersMancala(player)) {
                nextPlayerToMove = player;
            }

            if (isLastStep && pitWithOwner.isCurrentPlayersRegularPit(player) && pitWithOwner.pit().stonesInside() == 1) {
                int stonesFromOppositePit = boardWalker.fromOppositeSide().pit().takeAllStones();
                int stonesFromCurrentPit = pitWithOwner.pit().takeAllStones();
                boardState.mancalaPitFor(player).addStones(stonesFromOppositePit);
                boardState.mancalaPitFor(player).addStones(stonesFromCurrentPit);
                scoreAdded += stonesFromOppositePit + stonesFromCurrentPit;
            }
        }

        MoveLogItem log = boardState.logMove(player, pitNumber, nextPlayerToMove, scoreAdded, false);
        // Game is over.
        if (boardState.oneSideIsEmpty()) return new MoveResult(boardState, boardState.finishGame());

        return new MoveResult(boardState, log);
    }

    public MoveResult getCurrentState(BoardState boardState) throws MoveIsInvalidException {
        return boardState.lastMoveResult();
    }
}
