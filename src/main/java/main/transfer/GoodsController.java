package main.transfer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.mysql.cj.xdevapi.JsonArray;
import main.answers.StatusLoad;
import main.fileTransfer.FileUploader;
import main.model.catalog.Catalog;
import main.model.catalog.CatalogNodes;
import main.model.catalog.CatalogNodesRepository;
import main.model.catalog.CatalogRepository;
import main.model.propertyes.Property;
import main.model.propertyes.PropertyReposytory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    CatalogRepository catalogRepository;
    @Autowired
    CatalogNodesRepository catalogNodesRepository;
    @Autowired
    PropertyReposytory propertyReposytory;

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
}
