package ru.etozhealexis.client2.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.ip.udp.UnicastSendingMessageHandler;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UdpClient {

    private final UnicastSendingMessageHandler messageHandler;

    public void sendMessage(String message) {
        log.info("Sending message to host={} with port={}", messageHandler.getHost(), messageHandler.getPort());
        messageHandler.handleMessageInternal(MessageBuilder.withPayload(message).build());
    }
}
