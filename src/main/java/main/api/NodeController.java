package main.api;


import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.RequiredArgsConstructor;
import main.answers.StatusLoad;
import main.services.NodesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/node")
public class NodeController {

    //region FIELDS
    private final NodesService service;
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

}
