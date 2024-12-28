package ru.etozhealexis.client2.api;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.etozhealexis.client2.client.Client1Feign;
import ru.etozhealexis.client2.client.UdpClient;
import ru.etozhealexis.common.constatnts.Constants;
import ru.etozhealexis.common.service.EncodeService;
import ru.etozhealexis.common.service.ImageService;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;
    private final EncodeService encodeService;

    private final UdpClient udpClient;
    private final Client1Feign client1Feign;

    private final Environment environment;

    private String internalFormatImageFileName = Constants.CLIENT_2_INTERNAL_FORMAT_IMAGE_FILE_NAME;
    private String externalFormatFileName = Constants.CLIENT_2_EXTERNAL_FORMAT_IMAGE_FILE_NAME;

    @PostConstruct
    public void init() {
        if (environment.matchesProfiles("jpeg")) {
            internalFormatImageFileName += Constants.JPEG_FILE_EXTENSION;
            externalFormatFileName += Constants.JPEG_FILE_EXTENSION;
        }
    }

    @PutMapping("/generate")
    public void uploadImage() {
        imageService.generateAndSaveMatrix(Constants.CLIENT_2_INTERNAL_IMAGE_FILE_NAME);
    }

    @GetMapping("/internal")
    public String getInternalImage() {
        return imageService.getMatrix(Constants.CLIENT_2_INTERNAL_IMAGE_FILE_NAME);
    }

    @GetMapping("/external")
    public String getExternalImage() {
        encodeService.decode(externalFormatFileName, Constants.CLIENT_2_EXTERNAL_IMAGE_FILE_NAME);
        return imageService.getMatrix(Constants.CLIENT_2_EXTERNAL_IMAGE_FILE_NAME);
    }

    @SneakyThrows
    @PostMapping("/send")
    public void encodeAndSendImage() {
        encodeService.encode(Constants.CLIENT_2_INTERNAL_IMAGE_FILE_NAME, internalFormatImageFileName);
        udpClient.establishHandshake(client1Feign.getPublicKey());
        Thread.sleep(300);
        udpClient.sendImage(internalFormatImageFileName);
    }
}
