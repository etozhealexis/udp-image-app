package ru.etozhealexis.common.util;

import lombok.experimental.UtilityClass;

import java.util.zip.CRC32;

/**
 * A utility class for calculating checksum
 */
@UtilityClass
public class ChecksumUtil {

    public static long calculateChecksum(byte[] chunk) {
        CRC32 crc32 = new CRC32();
        crc32.update(chunk);
        return crc32.getValue();
    }
}
