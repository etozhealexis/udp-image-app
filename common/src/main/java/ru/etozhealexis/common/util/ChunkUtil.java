package ru.etozhealexis.common.util;

import lombok.experimental.UtilityClass;

/**
 * Byte array truncation crutch
 */
@UtilityClass
public class ChunkUtil {

    public static byte[] removeTrailingZeros(byte[] array) {
        int length = array.length;
        while (length > 0 && array[length - 1] == 0) {
            length--;
        }
        byte[] newArray = new byte[length];
        System.arraycopy(array, 0, newArray, 0, length);
        return newArray;
    }

}
