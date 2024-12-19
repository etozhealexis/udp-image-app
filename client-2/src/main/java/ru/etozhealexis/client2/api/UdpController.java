package ru.etozhealexis.client2.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.etozhealexis.client2.client.UdpClient;

@Slf4j
@RestController
@RequestMapping("/udp")
@RequiredArgsConstructor
public class UdpController {

    private final UdpClient udpClient;

    @PostMapping("/test")
    public void sendMessage() {
        log.info("Received test request. ");
        udpClient.sendMessage("YEAH IT'S A GOOOOOOL");
    }
}
