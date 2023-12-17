package com.permanovd.gamesessionservice.infrastructure;

import com.permanovd.gamesessionservice.domain.BoardWalker;
import com.permanovd.gamesessionservice.domain.Pit;
import com.permanovd.gamesessionservice.domain.Player;
import com.permanovd.gamesessionservice.domain.exceptions.PitOutOfBoardRangeException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BoardWalkerTest {

    @Test
    void walksBoardInCounterClockwiseDirection() throws PitOutOfBoardRangeException {
        int fromPit = 2;
        int boardSize = 6;
        Player owner = Player.ONE;
        var walker = new BoardWalker(
                Pit.generateEmpty(6, 6),
                Pit.generateEmpty(6, 6),
                boardSize,
                owner,
                fromPit);
        int i = 0;
        int expectedPitNumber = fromPit;
        while (i < 10000) {
            PitWithOwner withOwner = walker.next();
            assertThat(withOwner.pit().number()).isEqualTo(expectedPitNumber);
            assertThat(withOwner.owner()).isEqualTo(owner);
            if (expectedPitNumber == boardSize + 1) {
                // TODO Separate to another test.
                assertThat(withOwner.pit().isMancalaPit()).isTrue();
            }
            expectedPitNumber++;
            if (expectedPitNumber > boardSize + 1) {
                expectedPitNumber = 1;
                owner = owner.otherOne();
            }
            i++;
        }
    }

    @Test
    void returnsPitFromOppositeSideOfPlayerOne() throws PitOutOfBoardRangeException {
        int boardSize = 6;
        Player owner = Player.ONE;
        var walker = new BoardWalker(
                Pit.generateEmpty(6, 6),
                Pit.generateEmpty(6, 6),
                boardSize,
                owner,
                1);

        PitWithOwner pit = walker.next();
        assertThat(pit.pit().number()).isEqualTo(1);
        assertThat(walker.fromOppositeSide().pit().number()).isEqualTo(6);

        pit = walker.next();
        assertThat(pit.pit().number()).isEqualTo(2);
        assertThat(walker.fromOppositeSide().pit().number()).isEqualTo(5);

        pit = walker.next();
        assertThat(pit.pit().number()).isEqualTo(3);
        assertThat(walker.fromOppositeSide().pit().number()).isEqualTo(4);

        pit = walker.next();
        assertThat(pit.pit().number()).isEqualTo(4);
        assertThat(walker.fromOppositeSide().pit().number()).isEqualTo(3);

        pit = walker.next();
        assertThat(pit.pit().number()).isEqualTo(5);
        assertThat(walker.fromOppositeSide().pit().number()).isEqualTo(2);

        pit = walker.next();
        assertThat(pit.pit().number()).isEqualTo(6);
        assertThat(walker.fromOppositeSide().pit().number()).isEqualTo(1);
    }

    @Test
    void returnsPitFromOppositeSideOfPlayerTwo() throws PitOutOfBoardRangeException {
        int boardSize = 6;
        var walker = new BoardWalker(
                Pit.generateEmpty(6, 6),
                Pit.generateEmpty(6, 6),
                boardSize,
                Player.TWO,
                1);

        PitWithOwner pit = walker.next();
        assertThat(pit.pit().number()).isEqualTo(1);
        assertThat(walker.fromOppositeSide().pit().number()).isEqualTo(6);

        pit = walker.next();
        assertThat(pit.pit().number()).isEqualTo(2);
        assertThat(walker.fromOppositeSide().pit().number()).isEqualTo(5);

        pit = walker.next();
        assertThat(pit.pit().number()).isEqualTo(3);
        assertThat(walker.fromOppositeSide().pit().number()).isEqualTo(4);

        pit = walker.next();
        assertThat(pit.pit().number()).isEqualTo(4);
        assertThat(walker.fromOppositeSide().pit().number()).isEqualTo(3);

        pit = walker.next();
        assertThat(pit.pit().number()).isEqualTo(5);
        assertThat(walker.fromOppositeSide().pit().number()).isEqualTo(2);

        pit = walker.next();
        assertThat(pit.pit().number()).isEqualTo(6);
        assertThat(walker.fromOppositeSide().pit().number()).isEqualTo(1);
    }

    @Test
    void doesNotAllowToCreateIteratorIfStartPitIsInvalid() {
        assertThrows(PitOutOfBoardRangeException.class, () -> new BoardWalker(
                Pit.generateEmpty(6, 6),
                Pit.generateEmpty(6, 6),
                6,
                Player.ONE,
                0));
        assertThrows(PitOutOfBoardRangeException.class, () -> new BoardWalker(
                Pit.generateEmpty(6, 6),
                Pit.generateEmpty(6, 6),
                6,
                Player.ONE,
                7));
    }
}
