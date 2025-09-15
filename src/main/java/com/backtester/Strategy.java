package com.backtester;

import java.util.*;

public interface Strategy {
    boolean shouldBuy(List<Double> closes, int day);
    boolean shouldSell(List<Double> closes, int day);
}
