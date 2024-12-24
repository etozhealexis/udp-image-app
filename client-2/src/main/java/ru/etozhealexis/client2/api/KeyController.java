package ru.etozhealexis.client2.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.PublicKey;

@RestController
@RequestMapping("/key")
@RequiredArgsConstructor
public class KeyController {

    private final PublicKey publicKey;

    @GetMapping("/public")
    public byte[] getPublicKey() {
        return publicKey.getEncoded();
    }
}
