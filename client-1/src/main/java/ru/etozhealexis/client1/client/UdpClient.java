package ru.etozhealexis.client1.client;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.ip.udp.UnicastSendingMessageHandler;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Service;
import ru.etozhealexis.common.constatnts.Constants;
import ru.etozhealexis.common.service.KeyService;
import ru.etozhealexis.common.util.ChecksumUtil;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.file.Files;

/**
 * Class for sending messages via UDP
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UdpClient {

    private final KeyService keyService;
    private final UnicastSendingMessageHandler messageHandler;

    /**
     * Establishes handshake with client-2
     *
     * @param receiverPublicKeyBytes client-2 public key
     */
    @SneakyThrows
    public void establishHandshake(byte[] receiverPublicKeyBytes) {
        log.info("Establishing secret handshake to host={} with port={}. Begin",
                messageHandler.getHost(), messageHandler.getPort());
        byte[] encryptedKey = keyService.generateEncryptedKey(receiverPublicKeyBytes);
        messageHandler.handleMessageInternal(MessageBuilder.withPayload(encryptedKey).build());
        log.info("Establishing secret handshake to host={} with port={}. End",
                messageHandler.getHost(), messageHandler.getPort());
    }

    /**
     * Sends ciphered image to client-2.
     *
     * @param fileName image file name
     */
    @SneakyThrows
    public void sendImage(String fileName) {
        log.info("Sending message to host={} with port={}. Begin", messageHandler.getHost(), messageHandler.getPort());
        File imageFile = new File(fileName);
        byte[] imageBytes = Files.readAllBytes(imageFile.toPath());

        int totalPackets = (int) Math.ceil((double) imageBytes.length / (Constants.PACKET_SIZE - Constants.HEADER_SIZE));

        for (int packetId = 0; packetId < totalPackets; packetId++) {
            int start = packetId * (Constants.PACKET_SIZE - Constants.HEADER_SIZE);
            int end = Math.min(start + (Constants.PACKET_SIZE - Constants.HEADER_SIZE), imageBytes.length);
            byte[] chunk = new byte[end - start];
            System.arraycopy(imageBytes, start, chunk, 0, chunk.length);

            long calculatedChecksum = ChecksumUtil.calculateChecksum(chunk);

            ByteBuffer buffer = ByteBuffer.allocate(Constants.PACKET_SIZE);
            buffer.putInt(packetId);
            buffer.putInt(totalPackets);
            buffer.putLong(calculatedChecksum);
            buffer.put(chunk);

            byte[] encryptedPayload = keyService.encryptPayload(buffer.array());

            messageHandler.handleMessageInternal(MessageBuilder.withPayload(encryptedPayload).build());
        }
        log.info("Sending message to host={} with port={}. End",
                messageHandler.getHost(), messageHandler.getPort());
    }
}
