package com.backtester;

import java.util.*;

public class MovingAverageStrategy {
    private int shortWindow, longWindow;

    public MovingAverageStrategy(int shortWindow, int longWindow) {
        this.shortWindow = shortWindow;
        this.longWindow = longWindow;
    }

    public boolean shouldBuy(List<Double> closes, int day) {
        if (day < longWindow) return false;
        double shortMA = average(closes, day - shortWindow, day);
        double longMA = average(closes, day - longWindow, day);
        return shortMA > longMA;
    }

    public boolean shouldSell(List<Double> closes, int day) {
        if (day < longWindow) return false;
        double shortMA = average(closes, day - shortWindow, day);
        double longMA = average(closes, day - longWindow, day);
        return shortMA < longMA;
    }

    private double average(List<Double> closes, int start, int end) {
        double sum = 0;
        for (int i = start; i < end; i++) sum += closes.get(i);
        return sum / (end - start);
    }
}
