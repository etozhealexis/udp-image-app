package ru.etozhealexis.common.service;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import ru.etozhealexis.common.constatnts.Constants;

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
@Service
public class ImageService {

    private final Random random = new Random();

    /**
     * Generates a new matrix that contains pixels of an image
     */
    @SneakyThrows
    public void generateAndSaveMatrix(String fileName) {
        List<List<String>> generatedPixels = generateMatrix();
        try (CSVWriter writer = new CSVWriter(new FileWriter(fileName))) {
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
    public String getMatrix(String fileName) {
        try (Reader reader = Files.newBufferedReader(Path.of(fileName))) {
            try (CSVReader csvReader = new CSVReader(reader)) {
                return csvReader.readAll()
                        .stream()
                        .map(Arrays::asList)
                        .map(el -> String.join(",", el))
                        .collect(Collectors.joining("\n"));
            }
        }
    }

    /**
     * Generates matrix with {@link Constants.MATRIX_DIMENSION} x {@link Constants.MATRIX_DIMENSION} dimension
     *
     * @return generated matrix of RGB
     */
    private List<List<String>> generateMatrix() {
        List<List<String>> matrix = new ArrayList<>(Constants.MATRIX_DIMENSION);
        for (int i = 0; i < Constants.MATRIX_DIMENSION; i++) {
            matrix.add(generateLine());
        }
        return matrix;
    }

    /**
     * Generates a line with {@link Constants.MATRIX_DIMENSION} columns
     *
     * @return generated line
     */
    private List<String> generateLine() {
        List<String> line = new ArrayList<>(Constants.MATRIX_DIMENSION);
        for (int i = 0; i < Constants.MATRIX_DIMENSION; i++) {
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
