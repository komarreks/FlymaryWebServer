package main.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.RequiredArgsConstructor;

import main.answers.StatusLoad;
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

//    @PostMapping(value = "updatePrices", consumes = {MediaType.APPLICATION_JSON_VALUE})
//    public ResponseEntity updatePrices(@RequestBody ArrayNode jsPrices){
//        boolean ok = service.updatePrice(jsPrices);
//
//        if (ok){
//            return ResponseEntity.status(HttpStatus.CREATED).build();
//        }else {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
    //endregion

}
