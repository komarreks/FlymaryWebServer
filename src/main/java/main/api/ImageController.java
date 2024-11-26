package main.api;

import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.RequiredArgsConstructor;
import main.answers.StatusLoad;
import main.services.ImageService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/image")
@RequiredArgsConstructor
public class ImageController {
    //region FIELDS
    private final ImageService service;
    //endregion

    //region METHODS
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

    @GetMapping(value = "/{name}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImage(@PathVariable String name){
        byte[] imageBody = service.getImageByName(name);

        if(imageBody != null){
            return imageBody;
        }

        return null;
    }
    //endregion
}
