package ru.etozhealexis.client1.service;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ImageService {

    private static final String PIXELS_FILE_NAME = "data/client-1/pixels.csv";

    @SneakyThrows
    public void generateAndSaveImage() {
        List<List<String>> generatedPixels = generatePixels();
        try (CSVWriter writer = new CSVWriter(new FileWriter(PIXELS_FILE_NAME))) {
            generatedPixels.forEach(columns -> {
                String[] line = columns.toArray(String[]::new);
                writer.writeNext(line);
            });
        }
    }

    @SneakyThrows
    public String getImage() {
        try (Reader reader = Files.newBufferedReader(Path.of(PIXELS_FILE_NAME))) {
            try (CSVReader csvReader = new CSVReader(reader)) {
                String result = csvReader.readAll()
                        .stream()
                        .map(Arrays::asList)
                        .map(el -> String.join(",", el))
                        .collect(Collectors.joining("\n"));
                log.info(result);
                return result;
            }
        }
    }

    private List<List<String>> generatePixels() {
        return List.of(List.of("1.1.1", "255.255.255"), List.of("0.0.0", "200.173.221"));
    }
}
