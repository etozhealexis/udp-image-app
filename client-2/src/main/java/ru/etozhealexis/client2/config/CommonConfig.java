package ru.etozhealexis.client2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.etozhealexis.common.service.EncodeService;
import ru.etozhealexis.common.service.ImageService;
import ru.etozhealexis.common.service.KeyService;

/**
 * Common services configuration
 */
@Configuration
public class CommonConfig {

    @Bean
    public ImageService imageService() {
        return new ImageService();
    }

    @Bean
    public EncodeService encodeService() {
        return new EncodeService();
    }

    @Bean
    public KeyService keyService() {
        return new KeyService();
    }
}
