package ru.etozhealexis.client1.client;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.ip.udp.UnicastSendingMessageHandler;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import ru.etozhealexis.common.constatnts.Constants;

import java.io.File;
import java.nio.file.Files;
import java.util.zip.CRC32;

@Slf4j
@Service
@RequiredArgsConstructor
public class UdpClient {

    private final UnicastSendingMessageHandler messageHandler;

    public void sendMessage(String message) {
        log.info("Sending message to host={} with port={}", messageHandler.getHost(), messageHandler.getPort());
        messageHandler.handleMessageInternal(MessageBuilder.withPayload(message).build());
    }

    @SneakyThrows
    public void sendImage(String fileName) {
        log.info("Sending message to host={} with port={}. Begin", messageHandler.getHost(), messageHandler.getPort());
        File imageFile = new File(fileName);
        byte[] imageBytes = Files.readAllBytes(imageFile.toPath());

        CRC32 crc32 = new CRC32();
        crc32.update(imageBytes);
        long checksum = crc32.getValue();

        int totalPackets = (int) Math.ceil((double) imageBytes.length / (Constants.PACKET_SIZE - 12));

        for (int packetId = 0; packetId < totalPackets; packetId++) {
            int start = packetId * (Constants.PACKET_SIZE - 12);
            int end = Math.min(start + (Constants.PACKET_SIZE - 12), imageBytes.length);

            byte[] imageChunk = new byte[end - start];
            System.arraycopy(imageBytes, start, imageChunk, 0, end - start);

            byte[] metadata = createPacketMetadata(packetId, totalPackets, checksum);

            byte[] packet = new byte[metadata.length + imageChunk.length];
            System.arraycopy(metadata, 0, packet, 0, metadata.length);
            System.arraycopy(imageChunk, 0, packet, metadata.length, imageChunk.length);

            Message<byte[]> message = MessageBuilder.withPayload(packet).build();
            messageHandler.handleMessageInternal(message);
        }
        log.info("Sending message to host={} with port={}. End", messageHandler.getHost(), messageHandler.getPort());
    }

    private static byte[] createPacketMetadata(int packetId, int totalPackets, long checksum) {
        byte[] metadata = new byte[12];

        metadata[0] = (byte) (packetId >> 24);
        metadata[1] = (byte) (packetId >> 16);
        metadata[2] = (byte) (packetId >> 8);
        metadata[3] = (byte) packetId;

        metadata[4] = (byte) (totalPackets >> 24);
        metadata[5] = (byte) (totalPackets >> 16);
        metadata[6] = (byte) (totalPackets >> 8);
        metadata[7] = (byte) totalPackets;

        metadata[8] = (byte) (checksum >> 24);
        metadata[9] = (byte) (checksum >> 16);
        metadata[10] = (byte) (checksum >> 8);
        metadata[11] = (byte) checksum;

        return metadata;
    }
}
