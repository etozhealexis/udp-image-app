package ru.etozhealexis.client2.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.etozhealexis.common.service.ImageService;

@Slf4j
@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    private static final String PIXELS_FILE_NAME = "data/client-2/pixels.csv";

    @PutMapping("/generate")
    public void uploadImage() {
        imageService.generateAndSaveMatrix(PIXELS_FILE_NAME);
    }

    @GetMapping
    public String getImage() {
        return imageService.getMatrix(PIXELS_FILE_NAME);
    }

    @PostMapping("/send")
    public void encodeImage() {
        log.info("sending image");
//        imageService.sendImage();
    }
}
