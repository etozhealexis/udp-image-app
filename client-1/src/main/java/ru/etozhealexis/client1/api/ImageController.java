package ru.etozhealexis.client1.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.etozhealexis.client1.client.UdpClient;
import ru.etozhealexis.common.constatnts.Constants;
import ru.etozhealexis.common.service.EncodeService;
import ru.etozhealexis.common.service.ImageService;

@Slf4j
@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;
    private final EncodeService encodeService;

    private final UdpClient udpClient;

    @PutMapping("/generate")
    public void uploadImage() {
        imageService.generateAndSaveMatrix(Constants.CLIENT_1_INTERNAL_IMAGE_FILE_NAME);
    }

    @GetMapping("/internal")
    public String getInternalImage() {
        return imageService.getMatrix(Constants.CLIENT_1_INTERNAL_IMAGE_FILE_NAME);
    }

    @GetMapping("/external")
    public String getExternalImage() {
        return imageService.getMatrix(Constants.CLIENT_1_EXTERNAL_IMAGE_FILE_NAME);
    }

    @PostMapping("/send")
    public void encodeAndSendImage() {
        encodeService.encode(Constants.CLIENT_1_INTERNAL_IMAGE_FILE_NAME, Constants.CLIENT_1_INTERNAL_JPEG_IMAGE_FILE_NAME);
        udpClient.sendImage(Constants.CLIENT_1_INTERNAL_JPEG_IMAGE_FILE_NAME);
    }
}
