package ru.etozhealexis.client1.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.etozhealexis.client1.service.ImageService;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PutMapping("/generate")
    public void uploadImage() {
        imageService.generateAndSaveImage();
    }

    @GetMapping
    public String getImage() {
        return imageService.getImage();
    }
}
