package com.github.thesummergrinch.minesweeperanalyzer;

import com.github.thesummergrinch.minesweeperanalyzer.board.Board;
import com.github.thesummergrinch.minesweeperanalyzer.board.Board.BoardType;
import com.github.thesummergrinch.minesweeperanalyzer.data.DataWrapper;

public class AnalyzerRunnable implements Runnable {

    private final boolean[] numbersPresent;
    private final BoardType boardType;

    public AnalyzerRunnable(final BoardType boardType) {
        this.boardType = boardType;
        this.numbersPresent = new boolean[9];
    }

    @Override
    public void run() {
        Board board = new Board(new int[this.boardType.getWidth()][this.boardType.getHeight()], this.boardType.getBombs());
        board.generateBombs();
        final int[][] cells = board.cells();

        // Checks each cell, and sets the flag for the present numbers on the board.
        for (int[] cell : cells) {
            for (int j = 0; j < cells[0].length; j++) {
                if (cell[j] > 0) numbersPresent[cell[j]] = true;
            }
        }

        // Increments the statistic-tracker.
        for (int i = 1; i < 9; i++) {
            if (this.numbersPresent[i]) DataWrapper.incrementNumber(i);
        }

        DataWrapper.incrementBoardsSimulated();
    }
}
