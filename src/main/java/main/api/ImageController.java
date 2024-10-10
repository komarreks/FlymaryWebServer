package main.api;

import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.RequiredArgsConstructor;
import main.answers.StatusLoad;
import main.services.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/image")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService service;

    /**
     * Загрузка изображений
     * @param jsImages
     * @return
     */
    @PostMapping(value = "update", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity updateImages(@RequestBody ArrayNode jsImages){
        StatusLoad statusLoad = service.loadImages(jsImages);

        return ResponseEntity.status(HttpStatus.CREATED).body(statusLoad);
    }
}
