package com.backtester;

import java.io.*;
import java.util.*;

public class TradingBacktest {
    public static void main(String[] args) throws IOException {
        List<PriceData> data = loadCSV("/test/AAPL.csv"); // note the leading '/'
        MovingAverageStrategy strat = new MovingAverageStrategy(10, 50);
        Backtester backtester = new Backtester(10000);

        double finalValue = backtester.run(data, strat);
        System.out.println("Final portfolio value: $" + finalValue);
    }

    static List<PriceData> loadCSV(String resourcePath) throws IOException {
        List<PriceData> list = new ArrayList<>();

        // Use the class loader to get an InputStream
        try (InputStream is = TradingBacktest.class.getResourceAsStream(resourcePath);
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

            br.readLine(); // skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                list.add(new PriceData(parts[0],
                        Double.parseDouble(parts[1].substring(1, parts[1].length()-1)),
                        Double.parseDouble(parts[2].substring(1, parts[2].length()-1)),
                        Double.parseDouble(parts[3].substring(1, parts[3].length()-1)),
                        Double.parseDouble(parts[4].substring(1, parts[4].length()-1)),
                        Long.parseLong(parts[5].substring(1, parts[5].length()-1))));
            }
        }

        return list.reversed();
    }
}
