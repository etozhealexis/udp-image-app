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
        String previousRowColor = generateRandomBaseColor();

        for (int i = 0; i < Constants.MATRIX_DIMENSION; i++) {
            matrix.add(generateLine(previousRowColor));
            previousRowColor = matrix.get(i).get(0);
        }
        return matrix;
    }

    /**
     * Generates a line of pixels
     *
     * @param baseColor base color
     * @return line of radndom pixels
     */
    private List<String> generateLine(String baseColor) {
        List<String> line = new ArrayList<>(Constants.MATRIX_DIMENSION);
        String previousPixelColor = baseColor;

        for (int i = 0; i < Constants.MATRIX_DIMENSION; i++) {
            String newColor = generateSimilarColor(previousPixelColor);
            line.add(newColor);
            previousPixelColor = newColor;
        }
        return line;
    }

    /**
     * Generates similar color
     *
     * @param baseColor base color
     * @return similar to base color pixel
     */
    private String generateSimilarColor(String baseColor) {
        String[] parts = baseColor.split("\\.");
        int red = Integer.parseInt(parts[0]);
        int green = Integer.parseInt(parts[1]);
        int blue = Integer.parseInt(parts[2]);

        red = clamp(red + random.nextInt(11) - 5);
        green = clamp(green + random.nextInt(11) - 5);
        blue = clamp(blue + random.nextInt(11) - 5);

        return red + "." + green + "." + blue;
    }

    /**
     * Generates random base color
     *
     * @return randomly generated pixel
     */
    private String generateRandomBaseColor() {
        int red = random.nextInt(256);
        int green = random.nextInt(256);
        int blue = random.nextInt(256);
        return red + "." + green + "." + blue;
    }

    /**
     * Deletes colors that are out of bound
     *
     * @param value color value
     * @return bounded color value
     */
    private int clamp(int value) {
        return Math.max(0, Math.min(255, value));
    }
}
