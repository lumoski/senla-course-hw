package com.hotel.framework.util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CsvExporter<T> {
    private final String filePath;

    public CsvExporter(String filePath) {
        this.filePath = filePath;
    }

    public void export(List<T> items, CsvConverter<T> converter) {
        try (FileWriter writer = new FileWriter(filePath)) {

            writer.write(converter.getCsvHeaderLine() + "\n");

            for (T item : items) {
                writer.write(converter.getCsvDataLine(item) + "\n");
            }

            log.info("Exported to '{}'", filePath);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            System.err.println("Error in export to csv: " + e.getMessage());
        }
    }

    public interface CsvConverter<T> {
        String getCsvHeaderLine();

        String getCsvDataLine(T item);
    }
}