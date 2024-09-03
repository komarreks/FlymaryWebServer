package main.transfer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.node.ArrayNode;
import main.answers.LoadLine;
import main.answers.StatusLoad;
import main.fileTransfer.FileUploader;
import main.model.catalog.Catalog;
import main.model.catalog.CatalogNode;
import main.model.catalog.CatalogNodeRepository;
import main.model.catalog.CatalogRepository;
import main.model.catalog.nodechilddata.NodeProductRepository;
import main.model.goods.Product;
import main.model.goods.ProductReposytory;
import main.model.goods.characs.Charac;
import main.model.goods.characs.CharacRepository;
import main.model.images.Image;
import main.model.images.ImageRepository;
import main.model.propertyes.*;
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
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    CatalogRepository catalogRepository;
    @Autowired
    CatalogNodeRepository catalogNodesRepository;
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
    @Autowired
    NodeProductRepository nodeProductRepository;

    @PostMapping(value = "/updateCatalogs", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity updateCatalogs(@RequestBody ArrayNode catalogList){

        StatusLoad statusLoad = new StatusLoad();

        catalogList.forEach(jsonNode -> {
            String id1c = jsonNode.get("id1c").textValue().trim();

            LoadLine loadLine = new LoadLine(id1c);

            Catalog catalog = catalogRepository.findById1c(id1c);

            if (catalog == null){
                catalog = new Catalog();
                catalog.setId(UUID.randomUUID());
                catalog.setId1c(id1c);
                loadLine.setStatus("Загружен");
            }

            catalog.setDeleted(jsonNode.get("deleted").intValue());
            catalog.setVersion(jsonNode.get("version").asInt());
            catalog.setName(jsonNode.get("name").textValue());
            catalog.setTextButton(jsonNode.get("textButton").textValue());

            String image = jsonNode.get("image").textValue();
            catalog.setImagePath(FileUploader.safeImage(image, catalog.getName(), "jpg","catalogs"));

            ObjectReader reader = new ObjectMapper().readerFor(new TypeReference<List<String>>() {
            });

            StringBuilder sbStatusAdd = new StringBuilder();
            try {
                catalog.loadGoodPropertyes(reader.readValue(jsonNode.get("goodsPropertyes")));
            }catch (Exception e){
                sbStatusAdd.append("; ошибка загрузки списка свойств товаров");
            }

            try {
                catalog.loadcharacPropertyes(reader.readValue(jsonNode.get("characPropertyes")));
            }catch (Exception e){
                sbStatusAdd.append("; ошибка загрузки списка свойств характеристик");
            }

            catalogRepository.save(catalog);
            if (loadLine.getStatus() == null) loadLine.setStatus("Обновлен" + sbStatusAdd.toString());

            statusLoad.addLog(loadLine);
        });

        return ResponseEntity.status(HttpStatus.CREATED).body(statusLoad);
    }

    @PostMapping(value = "/updateNodes", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity updateNodes(@RequestBody ArrayNode nodesList){

        StatusLoad statusLoad = new StatusLoad();

        nodesList.forEach(jsonNode -> {
            int id = jsonNode.get("id").intValue();

            LoadLine loadLine = new LoadLine(jsonNode.get("id1c").textValue().trim());

            CatalogNode node = catalogNodesRepository.findById(id);

            if (node == null){
                node = new CatalogNode();
                node.setId(id);
                node.setId1c(jsonNode.get("id1c").textValue().trim());
                loadLine.setStatus("Загружен");
            }

            node.setVersion(jsonNode.get("version").intValue());
            node.setName(jsonNode.get("name").textValue());
            node.setSorting(jsonNode.get("sorting").intValue());
            node.setParent(catalogNodesRepository.findById(jsonNode.get("parent").intValue()));
            node.setCatalog(catalogRepository.findById1c(jsonNode.get("catalog").textValue()));
            node.setDeleted(jsonNode.get("deleted").intValue());

            String image = jsonNode.get("image").textValue();
            node.setImagePath(FileUploader.safeImage(image, node.getName(), "jpg","nodes"));

            ArrayNode productsId1c = (ArrayNode) jsonNode.get("products");
            nodeProductRepository.deleteAll(node.getProducts());
            for (JsonNode productId1c: productsId1c) {
                String id1c = productId1c.textValue();
                Product product = productReposytory.findById1c(id1c);
                node.addProduct(product);
            }

            catalogNodesRepository.save(node);

            if (loadLine.getStatus() == null) loadLine.setStatus("Обновлен");

            statusLoad.addLog(loadLine);
        });

        return ResponseEntity.status(HttpStatus.CREATED).body(statusLoad);
    }

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

    @PostMapping(value = "updateGoods", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity updateGoods(@RequestBody ArrayNode goodsList){
        StatusLoad statusLoad = new StatusLoad();

        goodsList.forEach(jsonNode -> {
            String id1c = jsonNode.get("id1c").textValue().trim();

            LoadLine loadLine = new LoadLine(id1c);

            Product product = productReposytory.findById1c(id1c);

            if (product == null){
                product = new Product();
                product.setId1c(id1c);
                product.setId(UUID.randomUUID());
                loadLine.setStatus("Загружен");
            }

            product.setName(jsonNode.get("name").textValue());

            product.clearPropertyes();

            Set<Map.Entry<String, JsonNode>> set =  jsonNode.get("propertyes").properties();

            for(Map.Entry<String, JsonNode> key: set){
                GoodPropertyValue newProperty = propertyValueReaper.findPropertyValue(product, key.getKey(), key.getValue().asText());

                product.addProperty(newProperty);
            }

            productReposytory.save(product);

            if (loadLine.getStatus() == null) loadLine.setStatus("Обновлен");
            statusLoad.addLog(loadLine);
        });

        return ResponseEntity.status(HttpStatus.CREATED).body(statusLoad);
    }

    @PostMapping(value = "updateCharacs", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity updateCharacs(@RequestBody ArrayNode characsList){
        StatusLoad statusLoad = new StatusLoad();

        characsList.forEach(jsonNode -> {
            String id1c = jsonNode.get("id1c").textValue().trim();
            String productId1c = jsonNode.get("productId1c").textValue().trim();

            LoadLine loadLine = new LoadLine(id1c);

            Charac charac = characRepository.findById1c(id1c);

            if (charac == null){
                charac = new Charac();
                charac.setId1c(id1c);
                charac.setId(UUID.randomUUID());
                loadLine.setStatus("Загружен");
            }

            if (charac.getProduct() == null) charac.setProduct(productReposytory.findById1c(productId1c));

            charac.setName(jsonNode.get("name").textValue());

            charac.clearPropertyes();

            Set<Map.Entry<String, JsonNode>> set =  jsonNode.get("propertyes").properties();

            for(Map.Entry<String, JsonNode> key: set){
                CharacPropertyValue newProperty = propertyValueReaper.findPropertyValue(charac, key.getKey(), key.getValue().asText());

                charac.addProperty(newProperty);
            }

            characRepository.save(charac);

            if (loadLine.getStatus() == null) loadLine.setStatus("Обновлен");
            statusLoad.addLog(loadLine);
        });

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
