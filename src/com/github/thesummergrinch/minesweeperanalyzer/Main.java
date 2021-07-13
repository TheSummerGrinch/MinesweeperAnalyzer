package com.github.thesummergrinch.minesweeperanalyzer;

import com.github.thesummergrinch.minesweeperanalyzer.board.Board.BoardType;
import com.github.thesummergrinch.minesweeperanalyzer.data.DataWrapper;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final int NUM_CORES = Math.max(Runtime.getRuntime().availableProcessors() - 1, 1);

    private static long simulations = 1_000_000L;
    private static BoardType boardType = BoardType.BEGINNER;

    private static void echoHelp() {
        System.out.println("java -jar MinesweeperAnalyzer.jar [OPTION <arg>]\nE.g. java -jar MinesweeperAnalyzer.jar -simulations 1000000000 -preset beginner" +
                "\t-simulations           <Long>:\tSets the amount of boards that should be simulated.\n" +
                "\t-preset                <classic-beginner | beginner | intermediate | expert>:\tSets the board-type that should be simulated.\n" +
                "\t-help:              \tprints this help-text.");
    }

    public static void main(String[] args) {

        for (int i = 0; i < args.length; i++) {
            switch (args[i].toLowerCase()) {
                case "-simulations" -> {
                    Main.simulations = Long.parseLong(args[i + 1]);
                    i += 1;
                }
                case "-preset" -> {
                    boardType = BoardType.fromString(args[i + 1]);
                    i += 1;
                }
                case "-help", "-h" -> {
                    echoHelp();
                    return;
                }
            }
        }

        // Init DataWrapper
        DataWrapper.init(Main.boardType);

        final ThreadPoolExecutor executor = new ThreadPoolExecutor(
                Main.NUM_CORES,
                Main.NUM_CORES,
                5000L,
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(10_000, false),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );

        long timeStart = System.currentTimeMillis();

        for (long x = 0; x < Main.simulations; x++) {
            executor.execute(new AnalyzerRunnable(Main.boardType));
        }

        executor.shutdown();

        System.out.println("Time taken: " + (((double) System.currentTimeMillis()) - timeStart) / 1_000 + "s");

        DataWrapper.printResults();

    }
}
