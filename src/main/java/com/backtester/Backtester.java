package com.backtester;

import java.util.*;

public class Backtester {
    private double cash;
    private final Map<String, Integer> sharesHeldPerStock = new HashMap<>();
    private final Map<String, List<PriceData>> stockData;
    private final Strategy strategy;
    public double value;
    private int dayIndex = 0;
    private final Map<String, Integer> startDayIndexes = new HashMap<>();
    private final Map<Integer, Date> indexedDates;

    public Backtester(double initialCash, Map<String, List<PriceData>> stockData, Strategy strategy, Map<Integer, Date> indexedDates) {
        this.cash = initialCash;
        this.stockData = stockData;
        this.value = cash;
        this.strategy = strategy;
        this.indexedDates = indexedDates;
    }

    public void step() {
        Date date = indexedDates.get(dayIndex);
        for (String stock : stockData.keySet()) {
            List<PriceData> data = stockData.get(stock);
            List<Double> closes = new ArrayList<>();
            for (PriceData d : data) closes.add(d.close);

            if (data.stream().anyMatch(a -> a.date.equals(date))) {
                startDayIndexes.putIfAbsent(stock, dayIndex);
                int relativeDayIndex = dayIndex - startDayIndexes.get(stock);

                double price = data.get(relativeDayIndex).close;
                int sharesHeld = sharesHeldPerStock.getOrDefault(stock, 0);
                if (strategy.shouldBuy(closes, relativeDayIndex) && cash >= price) {
                    // buy 1 share
                    cash -= price;
                    sharesHeld++;
                } else if (strategy.shouldSell(closes, relativeDayIndex) && sharesHeld > 0) {
                    // sell all shares
                    cash += sharesHeld * price;
                    sharesHeld = 0;
                }
                sharesHeldPerStock.put(stock, sharesHeld);
            }
        }

        // Final portfolio value
        double stocksValue = 0;
        for (String stock : sharesHeldPerStock.keySet()) {

            List<PriceData> data = stockData.get(stock);
            if (data.stream().anyMatch(a -> a.date.equals(date))) {
                int relativeDayIndex = dayIndex - startDayIndexes.get(stock);
                int sharesHeld = sharesHeldPerStock.get(stock);
                stocksValue += data.get(relativeDayIndex).close * sharesHeld;
            } else {
                //sells all stock if the data is no longer present (i,e: the year for that stock has passed)
                int relativeDayIndex = data.size() - 1;
                int sharesHeld = sharesHeldPerStock.get(stock);
                cash += sharesHeld * data.get(relativeDayIndex).close;
                sharesHeldPerStock.put(stock, 0);
            }

        }
        dayIndex++;
        value = cash + stocksValue;
        System.out.println("Day " + dayIndex + " portfolio value = " + value);
    }
}
