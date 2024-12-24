package ru.etozhealexis.client2.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.ip.udp.UnicastSendingMessageHandler;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UdpClient {

    private final UnicastSendingMessageHandler messageHandler;
}
