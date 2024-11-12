package main.api;

import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.RequiredArgsConstructor;

import main.answers.StatusLoad;
import main.services.ProductSevice;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/goods")
public class GoodsController {
    //region FIELDS
    private final ProductSevice service;
    //endregion

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
