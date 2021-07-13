package com.github.thesummergrinch.minesweeperanalyzer.data;

import com.github.thesummergrinch.minesweeperanalyzer.board.Board.BoardType;

import java.text.MessageFormat;
import java.util.concurrent.atomic.AtomicLong;

public class DataWrapper {

    private static final AtomicLong[] numbersFound = new AtomicLong[9];
    private static final AtomicLong boardsSimulated = new AtomicLong(0);
    private static BoardType boardType;

    public static void init(final BoardType boardType) {
        DataWrapper.boardType = boardType;
        for (int i = 0; i < 9; i++) {
            numbersFound[i] = new AtomicLong(0);
        }
    }

    public static void incrementNumber(int number) {
        DataWrapper.numbersFound[number].incrementAndGet();
    }

    public static void incrementBoardsSimulated() {
        DataWrapper.boardsSimulated.incrementAndGet();
    }

    public static void printResults() {
        for (int i = 1; i < 9; i++) {
            System.out.println(MessageFormat.format("The number {0} occurs {1}% of the {2}-boards. ({3} boards)",
                    i,
                    (double) DataWrapper.numbersFound[i].get() / DataWrapper.boardsSimulated.get() * 100.0,
                    DataWrapper.boardType.toString(),
                    DataWrapper.numbersFound[i].get()));
        }
    }

    public static Result getResult() {

        final float[] percentages = new float[9];
        long[] timesFoundNumber = new long[9];
        long simulations = DataWrapper.boardsSimulated.get();

        for (int i = 1; i < 9; i++) {
            timesFoundNumber[i] = DataWrapper.numbersFound[i].get();
        }

        for (int i = 1; i < 9; i++) {
            percentages[i] = (float) timesFoundNumber[i] / simulations * 100.0f;
        }

        return new Result(simulations, timesFoundNumber, percentages);

    }

}
