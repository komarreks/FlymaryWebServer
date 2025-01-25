package main.api;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import main.answers.StatusLoad;
import main.dto.*;
import main.model.catalog.CatalogNode;
import main.services.NodesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/node")
public class NodeController {

    //region FIELDS
    private final NodesService service;
    private final Mapper mapper;
    //endregion

    //region POSTS
    /**
     * Загрузка узлов каталога в формате json, так как родителькие объеты передаются
     * в виде строки идентификатора 1с
     * @param jsNodes
     * @return
     */
    @PostMapping(value = "/update", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity update(@RequestBody ArrayNode jsNodes){

        StatusLoad statusLoad = service.createUpdate(jsNodes);

        return ResponseEntity.status(HttpStatus.CREATED).body(statusLoad);
    }
    //endregion

    //region GET

    /**
     * Метод отдает категории каталога
     * @param catalog
     * @return
     */
    @GetMapping("/root{catalog}")
    public ResponseEntity getNodes(@PathParam(value = "catalog") String catalog){
        List<CatalogNode> catalogNodes = service.getRoot(catalog);
        return ResponseEntity.status(HttpStatus.OK).body(mapper.transferToNodesDTO(catalogNodes));
    }

    /**
     * Метод отдает товары категории
     * @param node
     * @return
     */
    @GetMapping(value = "/goods{node}")
    public ResponseEntity getGoods(@PathParam(value = "node") String node) throws JsonProcessingException {
        List<ProductDTO> productList = service.getProductsDTO(node);

        return ResponseEntity.status(HttpStatus.OK).body(productList);
    }

    @GetMapping(value = "/filters{node}")
    public ResponseEntity getFilter(@PathParam(value = "node") String node) throws JsonProcessingException {
        List<NodeDTO> filter = service.getChildNodesDTO(node);

        return ResponseEntity.status(HttpStatus.OK).body(filter);
    }

    @GetMapping(value = "/{id}/image", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImage(@PathVariable String id){
        byte[] imageBody = service.getImage(id);

        if(imageBody != null){
            return imageBody;
        }

        return null;
    }
    //endregion

}
