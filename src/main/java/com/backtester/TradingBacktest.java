package com.backtester;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

public class TradingBacktest {

    private static Map<String, List<PriceData>> stockData = new HashMap<>();
    private static List<Date> dates = new ArrayList<>();

    public static void main(String[] args) throws IOException {

        Path folder = Paths.get("src/main/resources/test");
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(folder, "*.csv")) {
            for (Path file : stream) {
                List<PriceData> data = loadCSV(file.toString());
                String stockName = file.toString().substring(24, file.toString().length()-4);
                stockData.put(stockName, data);
            }
        }

        Strategy movingAverageStrategy = new MovingAverageStrategy(10, 50);
        Strategy RSIStrategy = new RSIStrategy(14, 30, 70);

        double finalValue1 = 10000;
        double finalValue2 = 10000;

        dates = dates.reversed();
        int i = 0;
        Map<Integer, Date> indexedDates = new HashMap<>();
        for (Date date : dates) {
            indexedDates.put(i, date);
            i++;
        }

        Backtester backtesterMA = new Backtester(finalValue1, stockData, movingAverageStrategy, indexedDates);
        Backtester backtesterRSI = new Backtester(finalValue2, stockData, RSIStrategy, indexedDates);

        for (Date date : dates) {
            backtesterMA.step();
        }
        for (Date date : dates) {
            backtesterRSI.step();
        }

        System.out.println("Final portfolio value with MovingAverage: $" + backtesterMA.value);
        System.out.println("Final portfolio value with RSI: $" + backtesterRSI.value);
    }

    static List<PriceData> loadCSV(String filename) throws IOException {
        List<PriceData> list = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(filename))) {
            reader.skip(1);
            String[] parts;
            while (true) {
                try {
                    parts = reader.readNext();
                    if (parts == null) break;
                    Date date = new Date(parts[0]);
                    if (!dates.contains(date)) {
                        dates.add(date);
                    }
                    list.add(new PriceData(
                            date,
                            Double.parseDouble(parts[1]),
                            Double.parseDouble(parts[2]),
                            Double.parseDouble(parts[3]),
                            Double.parseDouble(parts[4]),
                            Long.parseLong(parts[5].replace(",", "")),
                            filename.substring(24, filename.length()-4)
                    ));
                } catch (CsvValidationException e) {
                    e.printStackTrace();
                }
            }
        }

        return list.reversed();
    }
}
