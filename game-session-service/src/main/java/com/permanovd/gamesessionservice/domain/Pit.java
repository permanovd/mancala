package com.permanovd.gamesessionservice.domain;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@NoArgsConstructor
@Data
@JsonIncludeProperties({"number", "stonesInside", "mancalaPit"})
public final class Pit implements Comparable<Pit> {
    private int number;
    private int stonesInside;
    private boolean isMancalaPit = false;

    public Pit(int number, int stonesInside, boolean isMancalaPit) {
        this.number = number;
        this.stonesInside = stonesInside;
        this.isMancalaPit = isMancalaPit;
    }

    public int number() {
        return number;
    }

    public void addStone() {
        stonesInside++;
    }

    public void addStones(int stones) {
        stonesInside = stonesInside + stones;
    }

    public int stonesInside() {
        return stonesInside;
    }

    public boolean isEmpty() {
        return stonesInside == 0;
    }

    public int takeAllStones() {
        int result = stonesInside;
        stonesInside = 0;
        return result;
    }

    public boolean isRegularPit() {
        return !isMancalaPit;
    }

    @Override
    public int compareTo(Pit o) {
        return Integer.compare(number, o.number);
    }

    public static SortedSet<Pit> generateEmpty(int count, int withStonesInside) {
        var result = new TreeSet<Pit>();
        for (int i = 1; i <= count; i++) {
            result.add(new Pit(i, withStonesInside, false));
        }
        result.add(new Pit(count + 1, 0, true));
        return result;
    }
}
