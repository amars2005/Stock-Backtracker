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
        //List<PriceData> allData = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(folder, "*.csv")) {
            for (Path file : stream) {
                List<PriceData> data = loadCSV(file.toString());
                String stockName = file.toString().substring(24, file.toString().length()-4);
                stockData.put(file.toString(), data);
            }
        }

        Strategy strat1 = new MovingAverageStrategy(10, 50);
        double finalValue1 = 10000;
        Backtester backtesterMA = new Backtester(finalValue1);

        for (Date date : dates) {
            for (List<PriceData> data: stockData.values()) {
                finalValue1 = backtesterMA.step(data, strat1, date);
            }
        }

        System.out.println("Final portfolio value with MovingAverage: $" + finalValue1);
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
