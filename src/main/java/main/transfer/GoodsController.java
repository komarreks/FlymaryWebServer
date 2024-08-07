package main.transfer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.node.ArrayNode;
import main.answers.StatusLoad;
import main.fileTransfer.FileUploader;
import main.model.catalog.Catalog;
import main.model.catalog.CatalogNodes;
import main.model.catalog.CatalogNodesRepository;
import main.model.catalog.CatalogRepository;
import main.model.goods.Product;
import main.model.goods.ProductReposytory;
import main.model.goods.characs.Charac;
import main.model.goods.characs.CharacRepository;
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
    CatalogNodesRepository catalogNodesRepository;
    @Autowired
    PropertyReposytory propertyReposytory;
    @Autowired
    ProductReposytory productReposytory;
    @Autowired
    PropertyValueReaper propertyValueReaper;
    @Autowired
    CharacRepository characRepository;

    @PostMapping(value = "/updateCatalogs", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity updateCatalogs(@RequestBody ArrayNode catalogList){

        StatusLoad statusLoad = new StatusLoad();
        statusLoad.setStatus(true);

        catalogList.forEach(jsonNode -> {
            String id1c = jsonNode.get("id1c").textValue().trim();

            Catalog catalog = catalogRepository.findById1c(id1c);
            int deleted = jsonNode.get("deleted").intValue();
            boolean modify = false;
            boolean delete = false;

            if (catalog == null && deleted == 0){
                catalog = new Catalog();
                catalog.setId1c(id1c);
                modify = true;
            } else if (catalog != null && deleted == 1) {
                catalogRepository.delete(catalog);
                delete = true;
            }

            if (!delete){
                catalog.setVersion(jsonNode.get("version").asInt());
                catalog.setName(jsonNode.get("name").textValue());
                catalog.setTextButton(jsonNode.get("textButton").textValue());

                String image = jsonNode.get("image").textValue();
                catalog.setImagePath(FileUploader.safeImage(image, catalog.getName(), "jpg","catalogs"));

                ObjectReader reader = new ObjectMapper().readerFor(new TypeReference<List<String>>() {
                });

                try {
                    catalog.loadGoodPropertyes(reader.readValue(jsonNode.get("goodsPropertyes")));
                }catch (Exception e){

                }

                try {
                    catalog.loadcharacPropertyes(reader.readValue(jsonNode.get("characPropertyes")));
                }catch (Exception e){

                }

                catalogRepository.save(catalog);
                modify = true;
            }

            if (modify || delete){
                statusLoad.addLoading(id1c);
            }

        });

        return ResponseEntity.status(HttpStatus.CREATED).body(statusLoad);
    }

    @PostMapping(value = "/updateNodes", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity updateNodes(@RequestBody ArrayNode nodesList){

        StatusLoad statusLoad = new StatusLoad();
        statusLoad.setStatus(true);

        nodesList.forEach(jsonNode -> {
            int id = jsonNode.get("id").intValue();

            CatalogNodes node = catalogNodesRepository.findById(id);
            int deleted = jsonNode.get("deleted").intValue();
            boolean modify = false;
            boolean delete = false;

            if (node == null && deleted == 0){
                node = new CatalogNodes();
                node.setId(id);
                node.setId1c(jsonNode.get("id1c").textValue().trim());
                modify = true;
            } else if (node != null && deleted == 1) {
                catalogNodesRepository.delete(node);
                delete = true;
            }

            if (!delete){
                node.setVersion(jsonNode.get("version").intValue());
                node.setName(jsonNode.get("name").textValue());
                node.setSorting(jsonNode.get("sorting").intValue());
                node.setParent(catalogNodesRepository.findById(jsonNode.get("parent").intValue()));
                node.setCatalog(catalogRepository.findById1c(jsonNode.get("catalog").textValue()));

                String image = jsonNode.get("image").textValue();
                node.setImagePath(FileUploader.safeImage(image, node.getName(), "jpg","nodes"));

                catalogNodesRepository.save(node);
                modify = true;
            }

            if (modify || delete){
                statusLoad.addLoading(node.getId1c());
            }

        });

        return ResponseEntity.status(HttpStatus.CREATED).body(statusLoad);
    }

    @PostMapping(value = "updatePropertyes", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity updatePropertyes(@RequestBody ArrayNode propertyList){
        StatusLoad statusLoad = new StatusLoad();
        statusLoad.setStatus(true);

        propertyList.forEach(jsonNode -> {
            String id1c = jsonNode.get("id1c").textValue().trim();

            Property property = propertyReposytory.findById1c(id1c);

            if (property == null){
                property = new Property();
                property.setId1c(id1c);
            }

            property.setName(jsonNode.get("name").textValue());

            propertyReposytory.save(property);

            statusLoad.addLoading(id1c);
        });

        return ResponseEntity.status(HttpStatus.CREATED).body(statusLoad);
    }

    @PostMapping(value = "updateGoods", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity updateGoods(@RequestBody ArrayNode goodsList){
        StatusLoad statusLoad = new StatusLoad();
        statusLoad.setStatus(true);

        goodsList.forEach(jsonNode -> {
            String id1c = jsonNode.get("id1c").textValue().trim();

            Product product = productReposytory.findById1c(id1c);

            if (product == null){
                product = new Product();
                product.setId1c(id1c);
            }

            product.setName(jsonNode.get("name").textValue());

            product.clearPropertyes();

            Set<Map.Entry<String, JsonNode>> set =  jsonNode.get("propertyes").properties();

            for(Map.Entry<String, JsonNode> key: set){
                GoodPropertyValue newProperty = propertyValueReaper.findPropertyValue(product, key.getKey(), key.getValue().asText());

                product.addProperty(newProperty);
            }

            productReposytory.save(product);

            statusLoad.addLoading(id1c);
        });

        return ResponseEntity.status(HttpStatus.CREATED).body(statusLoad);
    }

    @PostMapping(value = "updateCharacs", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity updateCharacs(@RequestBody ArrayNode characsList){
        StatusLoad statusLoad = new StatusLoad();
        statusLoad.setStatus(true);

        characsList.forEach(jsonNode -> {
            String id1c = jsonNode.get("id1c").textValue().trim();
            String productId1c = jsonNode.get("productId1c").textValue().trim();

            Charac charac = characRepository.findById1c(id1c);

            if (charac == null){
                charac = new Charac();
                charac.setId1c(id1c);
                charac.setProduct(productReposytory.findById1c(productId1c));
            }

            charac.setName(jsonNode.get("name").textValue());

            charac.clearPropertyes();

            Set<Map.Entry<String, JsonNode>> set =  jsonNode.get("propertyes").properties();

            for(Map.Entry<String, JsonNode> key: set){
                CharacPropertyValue newProperty = propertyValueReaper.findPropertyValue(charac, key.getKey(), key.getValue().asText());

                charac.addProperty(newProperty);
            }

            characRepository.save(charac);

            statusLoad.addLoading(id1c);
        });

        return ResponseEntity.status(HttpStatus.CREATED).body(statusLoad);
    }
}
