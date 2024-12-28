package ru.etozhealexis.common.service;

/**
 * Interface of encoding service
 */
public interface EncodeService {

    /**
     * Encodes image
     *
     * @param internalImageName       image file name
     * @param internalFormatImageName encoded image file name
     */
    void encode(String internalImageName, String internalFormatImageName);

    /**
     * Decodes image from JPEG
     *
     * @param externalFormatImageFileName encoded image file name
     * @param externalImageFileName       image file name
     */
    void decode(String externalFormatImageFileName, String externalImageFileName);
}
