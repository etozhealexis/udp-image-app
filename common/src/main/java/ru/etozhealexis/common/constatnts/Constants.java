package ru.etozhealexis.common.constatnts;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {

    public static final int MATRIX_DIMENSION = 80;
    public static final int PACKET_SIZE = 32;
    public static final int HEADER_SIZE = 16;

    /**
     * File names constants
     */
    public static final String CLIENT_1_INTERNAL_IMAGE_FILE_NAME = "data/client-1/internal/image.csv";
    public static final String CLIENT_1_INTERNAL_FORMAT_IMAGE_FILE_NAME = "data/client-1/internal/image";
    public static final String CLIENT_1_EXTERNAL_IMAGE_FILE_NAME = "data/client-1/external/image.csv";
    public static final String CLIENT_1_EXTERNAL_FORMAT_IMAGE_FILE_NAME = "data/client-1/external/image";

    public static final String CLIENT_2_INTERNAL_IMAGE_FILE_NAME = "data/client-2/internal/image.csv";
    public static final String CLIENT_2_INTERNAL_FORMAT_IMAGE_FILE_NAME = "data/client-2/internal/image";
    public static final String CLIENT_2_EXTERNAL_IMAGE_FILE_NAME = "data/client-2/external/image.csv";
    public static final String CLIENT_2_EXTERNAL_FORMAT_IMAGE_FILE_NAME = "data/client-2/external/image";

    public static final String JPEG_FILE_EXTENSION = ".jpeg";
}
