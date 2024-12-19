package ru.etozhealexis.client2.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Headers;

import java.util.Map;

@Slf4j
@MessageEndpoint
public class UdpInboundMessageHandler {

    @ServiceActivator(inputChannel = "inboundChannel")
    public void handeMessage(Message message, @Headers Map<String, Object> headerMap) {
        log.info("Received UDP message: {}", new String((byte[]) message.getPayload()));
    }
}
