package ru.etozhealexis.client1.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "client-2", url = "http://localhost:8082")
public interface Client2Feign {

    @GetMapping("/key/public")
    byte[] getPublicKey();
}
