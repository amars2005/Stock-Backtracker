package com.backtester;

import java.util.*;

public class Backtester {
    private double cash;
    private int sharesHeld;

    public Backtester(double initialCash) {
        this.cash = initialCash;
        this.sharesHeld = 0;
    }

    public double run(List<PriceData> data, Strategy strategy) {
        List<Double> closes = new ArrayList<>();
        for (PriceData d : data) closes.add(d.close);

        for (int day = 0; day < data.size(); day++) {
            double price = data.get(day).close;

            if (strategy.shouldBuy(closes, day) && cash >= price) {
                // buy 1 share
                cash -= price;
                sharesHeld++;
                System.out.println("Buy on " + data.get(day).date + " at " + price);
            } else if (strategy.shouldSell(closes, day) && sharesHeld > 0) {
                // sell all shares
                cash += sharesHeld * price;
                System.out.println("Sell on " + data.get(day).date + " at " + price);
                sharesHeld = 0;
            }
        }
        // Final portfolio value
        return cash + sharesHeld * data.get(data.size() - 1).close;
    }
}
