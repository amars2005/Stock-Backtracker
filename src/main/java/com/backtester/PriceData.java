package com.backtester;

public class PriceData {
    String date;
    double open, high, low, close;
    long volume;

    public PriceData(String date, double open, double high, double low, double close, long volume) {
        this.date = date;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
    }
}

