package ru.etozhealexis.client1.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.scheduling.annotation.Scheduled;
import ru.etozhealexis.common.constatnts.Constants;
import ru.etozhealexis.common.util.ChecksumUtil;
import ru.etozhealexis.common.util.ChunkUtil;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * UDP message handler
 */
@Slf4j
@MessageEndpoint
@RequiredArgsConstructor
public class UdpInboundMessageHandler {

    private ConcurrentMap<Integer, byte[]> packetMap = new ConcurrentHashMap<>();
    private int totalPackets = -1;
    private boolean checksumValidation = true;

    private SecretKey secretKey;

    private final PrivateKey privateKey;

    @SneakyThrows
    @ServiceActivator(inputChannel = "inboundChannel")
    public void handeMessage(Message<byte[]> message) {
        byte[] payload = message.getPayload();

        if (secretKey == null) {
            secretKey = decryptSecretKey(payload);
            log.info("Secret key successfully exchanged.");
            return;
        }

        byte[] decryptedPayload = decryptPayload(payload);

        ByteBuffer buffer = ByteBuffer.wrap(decryptedPayload);
        int packetId = buffer.getInt();
        totalPackets = buffer.getInt();
        long expectedChecksum = buffer.getLong();
        byte[] imageChunk = Arrays.copyOfRange(decryptedPayload, Constants.HEADER_SIZE, decryptedPayload.length);
        if (packetId == totalPackets - 1) {
            imageChunk = ChunkUtil.removeTrailingZeros(imageChunk);
        }
        long calculatedChecksum = ChecksumUtil.calculateChecksum(imageChunk);
        if (expectedChecksum != calculatedChecksum) {
            log.error("checksum mismatch, expectedChecksum={}, actualChecksum={}", expectedChecksum, calculatedChecksum);
            log.info("actualLen={}", imageChunk.length);
            checksumValidation = false;
        }
        packetMap.put(packetId, imageChunk);
        log.info("Receiving image request. {}/{}. size={}. {}/{} checksum",
                packetId, totalPackets - 1, packetMap.size(), expectedChecksum, calculatedChecksum);
    }

    @Scheduled(fixedRate = 1000)
    @SneakyThrows
    public void receive() {
        if (packetMap.size() == totalPackets) {
            log.info("All packets received. Reassembling image...");

            byte[] imageBytes = new byte[totalPackets * (Constants.PACKET_SIZE - Constants.HEADER_SIZE)];
            int offset = 0;
            for (int i = 0; i < totalPackets; i++) {
                byte[] chunk = packetMap.get(i);
                if (chunk == null) {
                    log.error("Missing packet " + i + ". Aborting.");
                    cleanData();
                    return;
                }
                System.arraycopy(chunk, 0, imageBytes, offset, chunk.length);
                offset += chunk.length;
            }

            if (!checksumValidation) {
                log.error("Checksum mismatch! Transmission corrupted.");
                cleanData();
                return;
            }

            saveImage(imageBytes);
            log.info("Image successfully received");
            cleanData();
        }
    }

    private void cleanData() {
        packetMap.clear();
        totalPackets = -1;
        checksumValidation = true;
        secretKey = null;
    }

    private static void saveImage(byte[] imageBytes) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(Constants.CLIENT_1_EXTERNAL_JPEG_IMAGE_FILE_NAME)) {
            fos.write(imageBytes);
        }
    }

    private SecretKey decryptSecretKey(byte[] encryptedKey)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher rsaCipher = Cipher.getInstance("RSA");
        rsaCipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedKey = rsaCipher.doFinal(encryptedKey);
        return new SecretKeySpec(decryptedKey, "AES");
    }

    private byte[] decryptPayload(byte[] payload)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return cipher.doFinal(payload);
    }
}
