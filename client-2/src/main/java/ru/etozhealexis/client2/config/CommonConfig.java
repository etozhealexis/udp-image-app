package ru.etozhealexis.client2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ru.etozhealexis.common.service.EncodeJPEGService;
import ru.etozhealexis.common.service.EncodePPMService;
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
    @Profile("jpeg")
    public EncodeService jpegEncodeService() {
        return new EncodeJPEGService();
    }

    @Bean
    @Profile("ppm")
    public EncodeService ppmEncodeService() {
        return new EncodePPMService();
    }

    @Bean
    public KeyService keyService() {
        return new KeyService();
    }
}
