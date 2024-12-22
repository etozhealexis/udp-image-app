package ru.etozhealexis.common.service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.etozhealexis.common.constatnts.Constants;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Class for encoding and decoding images
 */
@Slf4j
@Service
public class EncodeService {

    /**
     * Encodes image with JPEG
     *
     * @param internalImageName     image file name
     * @param internalJpegImageName JPEG image file name
     */
    public void encode(String internalImageName, String internalJpegImageName) {
        try {
            String line;
            BufferedImage image = new BufferedImage(Constants.MATRIX_DIMENSION, Constants.MATRIX_DIMENSION, BufferedImage.TYPE_INT_RGB);

            BufferedReader br = new BufferedReader(new FileReader(internalImageName));
            int i = 0;
            while ((line = br.readLine()) != null) {
                String[] pixels = line.split(",");
                for (int j = 0; j < pixels.length; j++) {
                    String pixel = pixels[j].replace("\"", "");
                    String[] rgb = pixel.split("\\.");
                    int r = Integer.parseInt(rgb[0]);
                    int g = Integer.parseInt(rgb[1]);
                    int b = Integer.parseInt(rgb[2]);

                    int color = (r << 16) | (g << 8) | b;
                    image.setRGB(j, i, color);
                }
                i++;
            }
            br.close();

            File outputFile = new File(internalJpegImageName);
            ImageIO.write(image, "jpeg", outputFile);
        } catch (IOException e) {
            log.info(e.getMessage());
        }
    }

    /**
     * Decodes image from JPEG
     *
     * @param externalJpegImageFileName JPEG image file name
     * @param externalImageFileName     image file name
     */
    @SneakyThrows
    public void decode(String externalJpegImageFileName, String externalImageFileName) {
        File imageFile = new File(externalJpegImageFileName);
        BufferedImage image = ImageIO.read(imageFile);

        int width = image.getWidth();
        int height = image.getHeight();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(externalImageFileName))) {
            for (int y = 0; y < height; y++) {
                StringBuilder row = new StringBuilder();
                for (int x = 0; x < width; x++) {
                    int color = image.getRGB(x, y);
                    int r = (color >> 16) & 0xFF;
                    int g = (color >> 8) & 0xFF;
                    int b = color & 0xFF;

                    String rgb = "\"" + r + "." + g + "." + b + "\"";

                    if (!row.isEmpty()) {
                        row.append(",");
                    }
                    row.append(rgb);
                }
                writer.write(row.toString());
                writer.newLine();
            }
        }
    }
}
