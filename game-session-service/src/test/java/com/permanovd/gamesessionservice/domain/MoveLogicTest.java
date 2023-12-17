package com.permanovd.gamesessionservice.domain;

import com.permanovd.gamesessionservice.domain.exceptions.GameIsOverException;
import com.permanovd.gamesessionservice.domain.exceptions.MoveIsInvalidException;
import com.permanovd.gamesessionservice.domain.exceptions.NotPlayersTurnToMoveException;
import com.permanovd.gamesessionservice.domain.exceptions.PitIsEmptyException;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MoveLogicTest {

    private MancalaGameService service = new MancalaGameService();

    @Test
    void increasesPlayerScoreIfStoneDroppedToMancala() throws MoveIsInvalidException {
        var board = BoardState.newDefaultSized();
        MoveResult move = service.move(board, Player.ONE, 2);

        assertThat(move.newBoardState().scoreFor(Player.ONE)).isEqualTo(1);
        assertThat(move.log().scoreAdded()).isEqualTo(1);
    }

    @Test
    void allowsAnotherMoveIfLastStoneLandedToPlayersMancala() throws MoveIsInvalidException {
        var board = BoardState.newDefaultSized();
        Player currentPlayer = Player.ONE;
        MoveResult move = service.move(board, currentPlayer, 1);

        assertThat(move.newBoardState().scoreFor(currentPlayer)).isEqualTo(1);
        assertThat(move.log().scoreAdded()).isEqualTo(1);
        assertThat(move.log().nextPlayerToMove()).isEqualTo(currentPlayer);
    }

    @Test
    void addsStonesToAllFollowingPitsOnOneSide() throws MoveIsInvalidException {
        var board = BoardState.newDefaultSized();
        Player currentPlayer = Player.ONE;
        service.move(board, currentPlayer, 1);
        Iterator<Pit> iterator = board.getPlayerOnePits().iterator();
        Pit first = iterator.next();
        assertThat(first.isEmpty()).isTrue();
        while (iterator.hasNext()) {
            Pit pit = iterator.next();
            if (pit.isMancalaPit()) assertThat(pit.stonesInside()).isEqualTo(1);
            else assertThat(pit.stonesInside()).isEqualTo(7);
        }
    }

    @Test
    void addsStonesToAllFollowingPitsOnBothSides() throws MoveIsInvalidException {
        int stonesCount = 6;
        var board = new BoardState(6, stonesCount);
        service.move(board, Player.ONE, 5);

        var p1Pits = board.getPlayerOnePits().stream().toList();
        var p2Pits = board.getPlayerTwoPits().stream().toList();
        // Player one side.
        assertThat(p1Pits.get(0).stonesInside()).isEqualTo(stonesCount);
        assertThat(p1Pits.get(1).stonesInside()).isEqualTo(stonesCount);
        assertThat(p1Pits.get(2).stonesInside()).isEqualTo(stonesCount);
        assertThat(p1Pits.get(3).stonesInside()).isEqualTo(stonesCount);
        assertThat(p1Pits.get(4).stonesInside()).isZero();
        assertThat(p1Pits.get(5).stonesInside()).isEqualTo(stonesCount + 1);
        assertThat(p1Pits.get(6).stonesInside()).isEqualTo(1); // Mancala.
        // Player two side.
        assertThat(p2Pits.get(0).stonesInside()).isEqualTo(stonesCount + 1);
        assertThat(p2Pits.get(1).stonesInside()).isEqualTo(stonesCount + 1);
        assertThat(p2Pits.get(2).stonesInside()).isEqualTo(stonesCount + 1);
        assertThat(p2Pits.get(3).stonesInside()).isEqualTo(stonesCount + 1);
        assertThat(p2Pits.get(4).stonesInside()).isEqualTo(stonesCount);
        assertThat(p2Pits.get(5).stonesInside()).isEqualTo(stonesCount);
        assertThat(p2Pits.get(6).stonesInside()).isZero(); // Mancala.
    }

    @Test
    void doesNotAddStonesToOtherPlayerMancala() throws MoveIsInvalidException {
        int stonesCount = 26;
        int size = 6;
        var board = new BoardState(size, stonesCount);
        service.move(board, Player.ONE, 5);
        int stonesAdded = stonesCount / (size * 2);

        var p1Pits = board.getPlayerOnePits().stream().toList();
        var p2Pits = board.getPlayerTwoPits().stream().toList();
        // Player one side.
        assertThat(p1Pits.get(0).stonesInside()).isEqualTo(stonesCount + stonesAdded);
        assertThat(p1Pits.get(1).stonesInside()).isEqualTo(stonesCount + stonesAdded);
        assertThat(p1Pits.get(2).stonesInside()).isEqualTo(stonesCount + stonesAdded);
        assertThat(p1Pits.get(3).stonesInside()).isEqualTo(stonesCount + stonesAdded);
        assertThat(p1Pits.get(4).stonesInside()).isEqualTo(2); // 2 full circles.
        assertThat(p1Pits.get(5).stonesInside()).isEqualTo(stonesCount + stonesAdded);
        assertThat(p1Pits.get(6).stonesInside()).isEqualTo(2); // Mancala.
        // Player two side.
        assertThat(p2Pits.get(0).stonesInside()).isEqualTo(stonesCount + stonesAdded);
        assertThat(p2Pits.get(1).stonesInside()).isEqualTo(stonesCount + stonesAdded);
        assertThat(p2Pits.get(2).stonesInside()).isEqualTo(stonesCount + stonesAdded);
        assertThat(p2Pits.get(3).stonesInside()).isEqualTo(stonesCount + stonesAdded);
        assertThat(p2Pits.get(4).stonesInside()).isEqualTo(stonesCount + stonesAdded);
        assertThat(p2Pits.get(5).stonesInside()).isEqualTo(stonesCount + stonesAdded);
        assertThat(p2Pits.get(6).stonesInside()).isZero(); // Mancala.
    }

    @Test
    void takesBothPlayersStonesToMancalaIfLandsLastOnEmptyPit() throws MoveIsInvalidException {
        int stonesCount = 13;
        int size = 6;
        var board = new BoardState(size, stonesCount);
        service.move(board, Player.ONE, 5);
        int stonesAdded = stonesCount / (size * 2);

        var p1Pits = board.getPlayerOnePits().stream().toList();
        var p2Pits = board.getPlayerTwoPits().stream().toList();
        // Player one side.
        assertThat(p1Pits.get(0).stonesInside()).isEqualTo(stonesCount + stonesAdded);
        assertThat(p1Pits.get(1).stonesInside()).isEqualTo(stonesCount + stonesAdded);
        assertThat(p1Pits.get(2).stonesInside()).isEqualTo(stonesCount + stonesAdded);
        assertThat(p1Pits.get(3).stonesInside()).isEqualTo(stonesCount + stonesAdded);
        assertThat(p1Pits.get(4).stonesInside()).isEqualTo(0); // 1 full circle + landed to empty.
        assertThat(p1Pits.get(5).stonesInside()).isEqualTo(stonesCount + stonesAdded);
        // 1 from going through mancala, 1 from p1 side stone, 13+1 from another players pit.
        assertThat(p1Pits.get(6).stonesInside()).isEqualTo(1 + stonesCount + stonesAdded + 1); // Mancala.
        // Player two side.
        assertThat(p2Pits.get(0).stonesInside()).isEqualTo(stonesCount + stonesAdded);
        assertThat(p2Pits.get(1).stonesInside()).isEqualTo(0); // Acquired by p1.
        assertThat(p2Pits.get(2).stonesInside()).isEqualTo(stonesCount + stonesAdded);
        assertThat(p2Pits.get(3).stonesInside()).isEqualTo(stonesCount + stonesAdded);
        assertThat(p2Pits.get(4).stonesInside()).isEqualTo(stonesCount + stonesAdded);
        assertThat(p2Pits.get(5).stonesInside()).isEqualTo(stonesCount + stonesAdded);
        assertThat(p2Pits.get(6).stonesInside()).isZero(); // Mancala.
    }

    @Test
    void doesNotAllowToMakeMoveIfPlayerHasNoRight() throws MoveIsInvalidException {
        var board = BoardState.newDefaultSized();
        Player currentPlayer = Player.ONE;
        MoveResult move = service.move(board, currentPlayer, 5);

        assertThat(move.log().nextPlayerToMove()).isEqualTo(currentPlayer.otherOne());
        assertThat(board.canMove(currentPlayer)).isFalse();
        assertThrows(NotPlayersTurnToMoveException.class, () -> service.move(board, currentPlayer, 5));
    }

    @Test
    void doesNotAllowToMakeMoveIfPitIsEmpty() throws MoveIsInvalidException {
        var board = new BoardState(6, 2);
        Player currentPlayer = Player.ONE;
        service.move(board, currentPlayer, 1);
        service.move(board, currentPlayer.otherOne(), 1);
        assertThrows(PitIsEmptyException.class, () -> service.move(board, currentPlayer, 1));
    }

    @Test
    void doesNotAllowToMakeMoveIfGameIsOver() throws MoveIsInvalidException {
        var board = BoardState.newDefaultSized();
        // Simulate empty board on second player side.
        board.getPlayerTwoPits().forEach(Pit::takeAllStones);
        service.move(board, Player.ONE, 1);

        assertThrows(GameIsOverException.class, () -> service.move(board, Player.TWO, 1));
    }

    @Test
    void collectsAllTheStonesToSidesWhenGameIsOver() throws MoveIsInvalidException {
        var board = BoardState.newDefaultSized();
        // 4 stones to mancala and one stone to last pit.
        board.getPlayerOnePits().forEach(Pit::takeAllStones);
        board.getPlayerOnePits().stream().toList().get(5).addStone();
        board.mancalaPitFor(Player.ONE).addStones(4);

        // 3 stones to last and first pits.
        board.getPlayerTwoPits().forEach(Pit::takeAllStones);
        board.getPlayerTwoPits().first().addStones(3);
        board.getPlayerTwoPits().stream().toList().get(2).addStones(3);
        board.mancalaPitFor(Player.TWO).addStones(1);

        // Making last move.
        MoveResult moveResult = service.move(board, Player.ONE, 6);

        assertThat(moveResult.log().gameIsOver()).isTrue();
        assertThat(board.scoreFor(Player.ONE)).isEqualTo(4 + 1);
        assertThat(board.scoreFor(Player.TWO)).isEqualTo(3 + 3 + 1);
    }
}
