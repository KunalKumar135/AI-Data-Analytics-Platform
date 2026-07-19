package com.example.demo.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Service
public class CsvReaderService {

    public List<String> getColumns(String filePath) throws IOException {

        BufferedReader reader =
                new BufferedReader(new FileReader(filePath));

        String header = reader.readLine();

        reader.close();

        return Arrays.asList(header.split(","));
    }

    public List<Map<String, String>> getData(String filePath)
            throws IOException {

        List<Map<String, String>> data = new ArrayList<>();

        BufferedReader reader =
                new BufferedReader(new FileReader(filePath));

        String headerLine = reader.readLine();

        if (headerLine == null) {
            reader.close();
            return data;
        }

        String[] headers = headerLine.split(",");

        String line;

        while ((line = reader.readLine()) != null) {

            String[] values = line.split(",");

            Map<String, String> row = new LinkedHashMap<>();

            for (int i = 0; i < headers.length; i++) {

                String value =
                        i < values.length ? values[i] : "";

                row.put(headers[i], value);
            }

            data.add(row);
        }

        reader.close();

        return data;
    }

    public Map<String, String> detectColumnTypes(
        String filePath) throws IOException {

        Map<String, String> columnTypes =
                new LinkedHashMap<>();

        BufferedReader reader =
                new BufferedReader(new FileReader(filePath));

        String headerLine =
                reader.readLine();

        String[] headers =
                headerLine.split(",");

        String sampleLine =
                reader.readLine();

        if (sampleLine == null) {

            reader.close();

            for (String header : headers) {

                columnTypes.put(
                        header,
                        "VARCHAR(255)");
            }

            return columnTypes;
        }

        String[] values =
                sampleLine.split(",");

        for (int i = 0; i < headers.length; i++) {

            String value =
                    i < values.length
                            ? values[i].trim()
                            : "";

            String type =
                    detectType(value);

            columnTypes.put(
                    headers[i],
                    type);
        }

        reader.close();

        return columnTypes;
    }
    private String detectType(
        String value) {

        if (value.matches("\\d+")) {

            return "INTEGER";
        }

        if (value.matches("\\d+\\.\\d+")) {

            return "NUMERIC";
        }

        if (value.matches(
                "\\d{4}-\\d{2}-\\d{2}")) {

            return "DATE";
        }

        return "VARCHAR(255)";
    }
}