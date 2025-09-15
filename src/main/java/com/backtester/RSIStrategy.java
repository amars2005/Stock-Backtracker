package com.backtester;

import java.util.*;

public class RSIStrategy implements Strategy{
    private int period;
    private double oversoldThreshold;
    private double overboughtThreshold;

    /**
     * @param period Number of days to calculate RSI (commonly 14)
     * @param oversoldThreshold RSI value below which to buy (commonly 30)
     * @param overboughtThreshold RSI value above which to sell (commonly 70)
     */
    public RSIStrategy(int period, double oversoldThreshold, double overboughtThreshold) {
        this.period = period;
        this.oversoldThreshold = oversoldThreshold;
        this.overboughtThreshold = overboughtThreshold;
    }

    @Override
    public boolean shouldBuy(List<Double> closes, int day) {
        if (day < period) return false;
        double rsi = calculateRSI(closes, day - period, day);
        return rsi < oversoldThreshold;
    }

    @Override
    public boolean shouldSell(List<Double> closes, int day) {
        if (day < period) return false;
        double rsi = calculateRSI(closes, day - period, day);
        return rsi > overboughtThreshold;
    }

    private double calculateRSI(List<Double> closes, int start, int end) {
        double gain = 0, loss = 0;
        for (int i = start + 1; i <= end; i++) {
            double change = closes.get(i) - closes.get(i - 1);
            if (change > 0) gain += change;
            else loss -= change; // loss is positive
        }
        double avgGain = gain / period;
        double avgLoss = loss / period;
        if (avgLoss == 0) return 100;
        double rs = avgGain / avgLoss;
        return 100 - (100 / (1 + rs));
    }
}
