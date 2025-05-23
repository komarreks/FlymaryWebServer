package main.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.RequiredArgsConstructor;

import main.answers.StatusLoad;
import main.dto.ProductDTO;
import main.services.ProductSevice;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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

    /**
     * метод обновления цен
     * @param jsPices
     * @return
     */
    @PostMapping(value = "/regularupdateprice", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity regularUpdatePrice(@RequestBody ArrayNode jsPices){
        StatusLoad statusLoad = service.regularUpdatePrice(jsPices);

        return ResponseEntity.status(HttpStatus.CREATED).body(statusLoad);
    }

    /**
     * обновление остатко
     * @param jsCounts
     * @return
     */
    @PostMapping(value = "/updatecounts", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity updateCounts(@RequestBody ArrayNode jsCounts){
        StatusLoad statusLoad = service.updateCounts(jsCounts);

        return ResponseEntity.status(HttpStatus.CREATED).body(statusLoad);
    }

    /**
     * Отдает DTO конкретного товара
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity getProduct(@PathVariable("id") String id){
        ProductDTO productDTO = service.getProductDTOByIdProduct(id);

        return ResponseEntity.status(HttpStatus.OK).body(productDTO);
    }



}
