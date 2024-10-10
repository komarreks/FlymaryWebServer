package main.api;

import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.RequiredArgsConstructor;
import main.answers.LoadLine;
import main.answers.StatusLoad;
import main.fileTransfer.FileUploader;
import main.model.goods.ProductReposytory;
import main.model.goods.characs.CharacRepository;
import main.model.images.Image;
import main.model.images.ImageRepository;
import main.model.propertyes.*;
import main.services.ProductSevice;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/goods")
public class GoodsController {
    //region FIELDS
    private final ProductSevice service;
    private final PropertyReposytory propertyReposytory;
    private final ProductReposytory productReposytory;
    private final CharacRepository characRepository;
    private final ImageRepository imageRepository;
    //endregion

    @Deprecated
    @PostMapping(value = "updatePropertyes", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity updatePropertyes(@RequestBody ArrayNode propertyList){
        StatusLoad statusLoad = new StatusLoad();

        propertyList.forEach(jsonNode -> {
            String id1c = jsonNode.get("id1c").textValue().trim();
            LoadLine loadLine = new LoadLine(id1c);
            Property property = propertyReposytory.findById1c(id1c);

            if (property == null){
                property = new Property();
                property.setId(UUID.randomUUID());
                property.setId1c(id1c);
                loadLine.setStatus("Загружен");
            }

            property.setName(jsonNode.get("name").textValue());

            propertyReposytory.save(property);

            if (loadLine.getStatus() != null) loadLine.setStatus("Обновлен");
            statusLoad.addLog(loadLine);
        });

        return ResponseEntity.status(HttpStatus.CREATED).body(statusLoad);
    }

    //region METHODS
    /**
     * создание/обновление товара
     * @param jsProducts
     * @return
     */
    @PostMapping(value = "/update", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity update(@RequestBody ArrayNode jsProducts){
        StatusLoad statusLoad = service.createUpdate(jsProducts);

        return ResponseEntity.status(HttpStatus.CREATED).body(statusLoad);
    }

    /**
     * Создание/обновление характеристик
     * @param jsCharacs
     * @return
     */
    @PostMapping(value = "updateCharacs", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity updateCharacs(@RequestBody ArrayNode jsCharacs){
        StatusLoad statusLoad = service.createUpdateCharacs(jsCharacs);

        return ResponseEntity.status(HttpStatus.CREATED).body(statusLoad);
    }
    //endregion

}
