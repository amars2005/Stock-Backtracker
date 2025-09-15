package com.backtester;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

public class TradingBacktest {
    public static void main(String[] args) throws IOException {

        Path folder = Paths.get("src/main/resources/test");
        List<List<PriceData>> allData = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(folder, "*.csv")) {
            for (Path file : stream) {
                List<PriceData> data = loadCSV(file.toString());
                System.out.println(file.toString());
                allData.add(data);
            }
        }

        Strategy strat1 = new MovingAverageStrategy(10, 50);
        Strategy strat2 = new RSIStrategy(14, 30, 70);
        double finalValue1 = 10000;
        double finalValue2 = 10000;
        Backtester backtesterMA = new Backtester(finalValue1);
        Backtester backtesterRSI = new Backtester(finalValue2);

        for (List<PriceData> data: allData) {
            finalValue1 = backtesterMA.run(data, strat1);
        }
        for (List<PriceData> data: allData) {
            finalValue2 = backtesterRSI.run(data, strat2);
        }
        System.out.println("Final portfolio value with MovingAverage: $" + finalValue1);
        System.out.println("Final portfolio value with RSI: $" + finalValue2);
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
                    list.add(new PriceData(
                            parts[0],
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
