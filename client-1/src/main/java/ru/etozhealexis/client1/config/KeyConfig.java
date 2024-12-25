package ru.etozhealexis.client1.config;

import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Key generation configuration
 */
@Configuration
public class KeyConfig {

    private static final int RSA_KEY_SIZE = 2048;

    @SneakyThrows
    @Bean
    public KeyPair keyPair() {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(RSA_KEY_SIZE);
        return keyPairGenerator.generateKeyPair();
    }

    @Bean
    public PublicKey publicKey() {
        return keyPair().getPublic();
    }

    @Bean
    public PrivateKey privateKey() {
        return keyPair().getPrivate();
    }
}
