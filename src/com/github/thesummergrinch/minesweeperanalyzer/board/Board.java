package com.github.thesummergrinch.minesweeperanalyzer.board;

import java.util.concurrent.ThreadLocalRandom;

public record Board(int[][] cells, int bombs) {

    public void generateBombs() {
        // Make a mutable copy of the bomb-int.
        int copyOfBombs = this.bombs;

        // While the number of bombs that haven't been placed on the board yet
        // is greater than 0, place bombs.
        while (copyOfBombs > 0) {

            int row = ThreadLocalRandom.current().nextInt(this.cells.length);
            int column = ThreadLocalRandom.current().nextInt(this.cells[0].length);

            if (this.cells[row][column] != -1) {

                // Set the value of a cell to -1, to signify that it is a bomb.
                this.cells[row][column] = -1;
                copyOfBombs--;

                // Loop through the surrounding cells
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {

                        if (isValidCell(row + i, column + j)
                                && this.cells[row + i][column + j] != -1)

                            // If the cell is valid, and not a bomb, increment
                            // the cell's value, which signifies the number of
                            // surrounding bombs;
                            this.cells[row + i][column + j] += 1;
                    }
                }
            }
        }
    }

    /**
     * Checks that the given coordinates are withing the bounds of the record's
     * cell-array.
     * @param row
     * @param column
     * @return true if within bounds, false otherwise.
     */
    private boolean isValidCell(final int row, final int column) {
        return row >= 0
                && row < this.cells.length
                && column >= 0
                && column < this.cells[0].length;
    }

    public enum BoardType {

        CLASSIC_BEGINNER(8, 8, 10),
        BEGINNER(9, 9, 10),
        INTERMEDIATE(16, 16, 40),
        EXPERT(30, 16, 99);

        private final int width, height, bombs;

        BoardType(final int width, final int height, final int bombs) {
            this.width = width;
            this.height = height;
            this.bombs = bombs;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public int getBombs() {
            return bombs;
        }

        @Override
        public String toString() {
            switch (this) {
                case CLASSIC_BEGINNER -> {
                    return "8 x 8 - Beginner";
                }
                case BEGINNER -> {
                    return "9 x 9 - Beginner";
                }
                case INTERMEDIATE -> {
                    return "Intermediate";
                }
                case EXPERT -> {
                    return "Expert";
                }
                default -> throw new RuntimeException("Illegal Enum in BoardType.java");
            }
        }

        public static BoardType fromString(final String boardTypeString) {
            switch (boardTypeString.toLowerCase()) {
                case "classic-beginner" -> {
                    return CLASSIC_BEGINNER;
                }
                case "beginner" -> {
                    return BEGINNER;
                }
                case "intermediate" -> {
                    return INTERMEDIATE;
                }
                case "expert" -> {
                    return EXPERT;
                }
                default -> {
                    System.out.println("Did not recognize -preset option. " +
                            "Initializing 9 x 9 - Beginner board simulation.");
                    return BEGINNER;
                }
            }
        }
    }

}
