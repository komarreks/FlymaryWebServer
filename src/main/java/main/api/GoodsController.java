package main.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.RequiredArgsConstructor;
import main.answers.LoadLine;
import main.answers.StatusLoad;
import main.fileTransfer.FileUploader;
import main.model.goods.ProductReposytory;
import main.model.goods.characs.Charac;
import main.model.goods.characs.CharacRepository;
import main.model.goods.characs.characproperty.CharacPropertyValue;
import main.model.images.Image;
import main.model.images.ImageRepository;
import main.model.propertyes.*;
import main.services.ProductSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/goods")
public class GoodsController {

    @Autowired
    private final ProductSevice service;

    @Autowired
    PropertyReposytory propertyReposytory;
    @Autowired
    ProductReposytory productReposytory;
    @Autowired
    PropertyValueReaper propertyValueReaper;
    @Autowired
    CharacRepository characRepository;
    @Autowired
    ImageRepository imageRepository;

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

    @PostMapping(value = "/update", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity update(@RequestBody ArrayNode goodsList){
        StatusLoad statusLoad = service.createUpdate(goodsList);

        return ResponseEntity.status(HttpStatus.CREATED).body(statusLoad);
    }

    @PostMapping(value = "updateCharacs", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity updateCharacs(@RequestBody ArrayNode jsCharacs){
        StatusLoad statusLoad = service.createUpdateCharacs(jsCharacs);

        return ResponseEntity.status(HttpStatus.CREATED).body(statusLoad);
    }

    @PostMapping(value = "updateImages", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity updateImages(@RequestBody ArrayNode imageList){
        StatusLoad statusLoad = new StatusLoad();

        imageList.forEach(jsonNode -> {
            String id1c = jsonNode.get("id1c").textValue().trim();

            LoadLine loadLine = new LoadLine(id1c);

            Image image = imageRepository.findById1c(id1c);

            if (image == null){
                image = new Image();
                image.setId1c(id1c);
                image.setId(UUID.randomUUID());
                image.setProduct(productReposytory.findById1c(jsonNode.get("product_id1c").textValue()));
                image.setCharac(characRepository.findById1c(jsonNode.get("charac_id1c").textValue()));
                image.setName(jsonNode.get("name").textValue());

                String imageBody = jsonNode.get("image").textValue();
                image.setPath(FileUploader.safeImage(imageBody, image.getName(), "jpg","goods_img"));
                imageRepository.save(image);
                loadLine.setStatus("Загружен");
            }

            image.setDeleted(jsonNode.get("deleted").intValue());

            if (loadLine.getStatus() == null) loadLine.setStatus("");
            statusLoad.addLog(loadLine);
        });

        return ResponseEntity.status(HttpStatus.CREATED).body(statusLoad);
    }
}
