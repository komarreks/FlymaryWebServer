package main.transfer;

import com.fasterxml.jackson.databind.node.ArrayNode;
import main.answers.StatusLoad;
import main.model.goods.Catalog;
import main.model.goods.CatalogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    CatalogRepository catalogRepository;

    @PostMapping(value = "/update", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity updateCatalogs(@RequestBody ArrayNode catalogList){

        StatusLoad statusLoad = new StatusLoad();
        statusLoad.setStatus(true);
        statusLoad.setError("");

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
                catalog.setVersion(jsonNode.get("version").intValue());
                catalog.setName(jsonNode.get("name").textValue());
                catalog.setTextButton(jsonNode.get("textButton").textValue());
                catalogRepository.save(catalog);
                modify = true;
            }

            if (modify || delete){
                statusLoad.addLoading(id1c);
            }

        });

        return ResponseEntity.status(HttpStatus.CREATED).body(statusLoad);
    }
}
