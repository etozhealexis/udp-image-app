package ru.etozhealexis.common.service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for encoding and decoding PPM images
 */
@Slf4j
@Service
public class EncodePPMService implements EncodeService {

    @SneakyThrows
    public void encode(String csvFileName, String internalFormatImageName) {
        List<String> lines = Files.readAllLines(Paths.get(csvFileName));

        int width = lines.get(0).split(",").length;
        int height = lines.size();
        int maxColorValue = 255;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(internalFormatImageName))) {
            writer.write("P3\n");
            writer.write(width + " " + height + "\n");
            writer.write(maxColorValue + "\n");

            for (String line : lines) {
                String[] pixels = line.split(",");
                for (String pixel : pixels) {
                    String[] rgb = pixel.replace("\"", "").split("\\.");
                    writer.write(rgb[0] + " " + rgb[1] + " " + rgb[2] + " ");
                }
                writer.newLine();
            }
        }
    }

    @SneakyThrows
    public void decode(String ppmFileName, String csvFileName) {
        List<String> lines = Files.readAllLines(Paths.get(ppmFileName));
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFileName))) {
            String[] dimensions = lines.get(1).split(" ");
            int width = Integer.parseInt(dimensions[0]);

            List<String> pixelData = lines.subList(3, lines.size());
            List<String> row = new ArrayList<>();
            List<Integer> rgbBuffer = new ArrayList<>();

            for (String line : pixelData) {
                String sanitizedLine = line.replace("\0", "").trim();
                if (sanitizedLine.isEmpty()) {
                    continue;
                }

                String[] values = sanitizedLine.split("\\s+");
                for (String value : values) {
                    if (value.isEmpty()) {
                        continue;
                    }

                    try {
                        rgbBuffer.add(Integer.parseInt(value));

                        if (rgbBuffer.size() == 3) {
                            String pixel = "\"" + rgbBuffer.get(0) + "." + rgbBuffer.get(1) + "." + rgbBuffer.get(2) + "\"";
                            row.add(pixel);
                            rgbBuffer.clear();

                            if (row.size() == width) {
                                writer.write(String.join(",", row));
                                writer.newLine();
                                row.clear();
                            }
                        }
                    } catch (NumberFormatException e) {
                        log.warn("Warning: Invalid pixel value \"" + value + "\" skipped.");
                    }
                }
            }

            if (!row.isEmpty()) {
                writer.write(String.join(",", row));
                writer.newLine();
            }
        }
    }
}
