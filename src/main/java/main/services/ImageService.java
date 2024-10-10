package main.services;

import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.RequiredArgsConstructor;
import main.answers.LoadLine;
import main.answers.StatusLoad;
import main.model.images.Image;
import main.model.images.ImageRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ImageService {

    //region FIELDS
    private final ImageRepository repository;
    private final ProductSevice productSevice;
    //endregion

    //region METHODS
    /**
     * Метод загрузки / удаления изображений с флаогм deleted
     * @param jsImages
     * @return
     */
    public StatusLoad loadImages(ArrayNode jsImages){
        StatusLoad statusLoad = new StatusLoad();

        jsImages.forEach(jsonNode -> {
            String id1c = jsonNode.get("id1c").textValue().trim();

            LoadLine loadLine = new LoadLine(id1c);

            Image image = repository.findById1c(id1c);

            if (image == null){
                image = new Image();
                image.setId1c(id1c);
                image.setProduct(productSevice.findById1c(jsonNode.get("product_id1c").textValue()));
                image.setCharac(productSevice.findCharacById1c(jsonNode.get("charac_id1c").textValue()));
                image.setName(jsonNode.get("name").textValue());

                try {
                    image.setImage64(jsonNode.get("image64").binaryValue());
                } catch (IOException e) {
                    image.setImage64(null);
                }

                image.setDeleted(jsonNode.get("deleted").intValue());

                repository.save(image);
                loadLine.setStatus("Загружен");
            } else if (jsonNode.get("deleted").intValue() == 1) {
                repository.delete(image);
                loadLine.setStatus("Удален");
            }

            if (loadLine.getStatus() == null) loadLine.setStatus("");
            statusLoad.addLog(loadLine);
        });

        return statusLoad;
    }
    //endregion
}
