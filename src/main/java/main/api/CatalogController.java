package main.api;

import lombok.RequiredArgsConstructor;
import main.answers.LoadLine;
import main.answers.StatusLoad;
import main.model.catalog.Catalog;
import main.model.catalog.CatalogRepository;
import main.services.CatalogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/catalog")
@RequiredArgsConstructor
public class CatalogController {

    //region FIELDS
    private final CatalogService service;
    //endregion

    //region GET
    @GetMapping("/all")
    public ResponseEntity all(){
        return ResponseEntity.status(HttpStatus.OK).body(service.getAll());
    }
    //endregion

    //region POSTS
    @PostMapping("/update")
    public ResponseEntity update(@RequestBody List<Catalog> catalogs){

        StatusLoad statusLoad = service.createUpdateCatalogs(catalogs);

        return ResponseEntity.status(HttpStatus.CREATED).body(statusLoad);
    }
    //endregion



}
