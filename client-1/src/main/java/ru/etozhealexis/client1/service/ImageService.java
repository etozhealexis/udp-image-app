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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * A class for working with image generation
 */
@Slf4j
@Service
public class ImageService {

    private static final String PIXELS_FILE_NAME = "data/client-1/pixels.csv";
    private static final int MATRIX_DIMENSION = 800;
    private final Random random = new Random();

    /**
     * Generates a new matrix that contains pixels of an image
     */
    @SneakyThrows
    public void generateAndSaveMatrix() {
        List<List<String>> generatedPixels = generateMatrix();
        try (CSVWriter writer = new CSVWriter(new FileWriter(PIXELS_FILE_NAME))) {
            generatedPixels.forEach(columns -> {
                String[] line = columns.toArray(String[]::new);
                writer.writeNext(line);
            });
        }
    }

    /**
     * Returns matrix that contains pixels of an image
     *
     * @return matrix
     */
    @SneakyThrows
    public String getMatrix() {
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

    /**
     * Generates matrix if {@link MATRIX_DIMENSION} x {@link MATRIX_DIMENSION} dimension
     *
     * @return generated matrix of RGB
     */
    private List<List<String>> generateMatrix() {
        List<List<String>> matrix = new ArrayList<>(MATRIX_DIMENSION);
        for (int i = 0; i < MATRIX_DIMENSION; i++) {
            matrix.add(generateLine());
        }
        return matrix;
    }

    /**
     * Generates a line with {@link MATRIX_DIMENSION} columns
     *
     * @return generated line
     */
    private List<String> generateLine() {
        List<String> line = new ArrayList<>(MATRIX_DIMENSION);
        for (int i = 0; i < MATRIX_DIMENSION; i++) {
            line.add(generateColumn());
        }
        return line;
    }

    /**
     * Generates column, consisting of RGB values
     *
     * @return generated column
     */
    private String generateColumn() {
        int red = random.nextInt(256);
        int green = random.nextInt(256);
        int blue = random.nextInt(256);
        return red + "." + green + "." + blue;
    }
}
