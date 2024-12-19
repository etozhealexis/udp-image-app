package ru.etozhealexis.client2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.etozhealexis.common.service.ImageService;

@Configuration
public class CommonConfig {

    @Bean
    public ImageService imageService() {
        return new ImageService();
    }
}
