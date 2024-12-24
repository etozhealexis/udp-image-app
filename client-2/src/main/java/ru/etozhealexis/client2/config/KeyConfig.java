package ru.etozhealexis.client2.config;

import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

@Configuration
public class KeyConfig {

    private static final int RSA_KEY_SIZE = 2048;

    @SneakyThrows
    @Bean
    @Scope("singleton")
    public KeyPair keyPair() {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(RSA_KEY_SIZE);
        return keyPairGenerator.generateKeyPair();
    }

    @Bean
    @Scope("prototype")
    public PublicKey publicKey() {
        return keyPair().getPublic();
    }

    @Bean
    @Scope("prototype")
    public PrivateKey privateKey() {
        return keyPair().getPrivate();
    }
}
