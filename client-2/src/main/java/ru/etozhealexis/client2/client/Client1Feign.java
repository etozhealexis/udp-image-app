package ru.etozhealexis.client2.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

@Component
@FeignClient(value = "client-1", url = "http://localhost:8081")
public interface Client1Feign {

    @GetMapping("/key/public")
    byte[] getPublicKey();
}
