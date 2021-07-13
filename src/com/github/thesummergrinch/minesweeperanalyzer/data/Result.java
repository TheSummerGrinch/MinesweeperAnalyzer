package com.github.thesummergrinch.minesweeperanalyzer.data;

public record Result (long simulations, long[] found, float[] percentages) {}