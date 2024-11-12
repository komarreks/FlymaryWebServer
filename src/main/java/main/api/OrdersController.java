package main.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import main.answers.SimpleAnswer;
import main.answers.StatusLoad;
import main.model.orders.*;
import main.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@RequestMapping("/orders")
public class OrdersController {
    //region FIELDS
    @Autowired
    OrderService service;
    //endregion

    //region REST API METHODS
    @PostMapping(value = "/updateOrders", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity updateOrders(@RequestBody ArrayNode jsOrders){
        StatusLoad statusLoad = service.loadChangedOrders(jsOrders);

        return ResponseEntity.status(HttpStatus.CREATED).body(statusLoad);
    }

    @GetMapping(value = "/createOrder", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity createOrder(@RequestBody JsonNode jsUserId){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createNewOrder(jsUserId).getId());
    }

    @PostMapping(value = "/addLine", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity addLine(@RequestBody JsonNode jsLine){
        SimpleAnswer sa = service.addLine(jsLine);

        if (sa.isError()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(sa.getText());
        }
        return ResponseEntity.status(HttpStatus.OK).body(sa.getText());
    }

    @PostMapping(value = "/changeCount", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity changeCount(@RequestBody JsonNode jsLine){
        SimpleAnswer sa = service.changeCountInLine(jsLine);

        if (sa.isError()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(sa.getText());

        return ResponseEntity.status(HttpStatus.OK).body(sa.getText());
    }

    @DeleteMapping(value = "/deleteLine")
    public ResponseEntity deleteLine(@RequestBody JsonNode jsLine){
        SimpleAnswer sa = service.deleteLine(jsLine);

        if (sa.isError()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(sa.getText());

        return ResponseEntity.status(HttpStatus.OK).body(sa.getText());
    }

    @DeleteMapping(value = "/deleteOrder")
    public ResponseEntity deleteOrder(@RequestBody JsonNode jsLine){
        SimpleAnswer sa = service.deleteOrder(jsLine);

        if (sa.isError()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(sa.getText());

        return ResponseEntity.status(HttpStatus.OK).body(sa.getText());
    }

    @GetMapping(value = "/getOpenOrders")
    public ResponseEntity getOpenOrders(){
        List<Order> openedOrders = getOrdersWithStatus(OrderStatus.OPEN);

        ObjectNode response = createOrderJsonArray(openedOrders);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = "/getFinishedOrders")
    public ResponseEntity getFinishedOrders(){
        List<Order> openedOrders = getOrdersFinished();

        ObjectNode response = createOrderJsonArray(openedOrders);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(value = "/finalizeOrder")
    public ResponseEntity finalizeOrder(@RequestBody JsonNode jsOrder){
        SimpleAnswer sa = service.finallyOrder(jsOrder);

        if (sa.isError()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(sa.getText());

        return ResponseEntity.status(HttpStatus.OK).body(sa.getText());
    }
    //endregion

    //region OTHER METHODS

    /**
     * Метод возвращает список заказов с определенным статусом
     * @param orderStatus
     * @return
     */
    private List<Order> getOrdersWithStatus(OrderStatus orderStatus){
        return service.findByStatus(orderStatus);
    }

    /**
     * Метод возвращает зафинализированные заказы
     * @return
     */
    private List<Order> getOrdersFinished(){
        return getOrdersWithStatus(OrderStatus.IN_WORK).stream()
                .filter(order -> order.getId1c() == null).toList();
    }

    /**
     * Метод конвертирует сущности заказов в образ JSON
     * пока располагается здесь, возможно потом будет вынесен в DTO класс
     * @param orders
     * @return
     */
    private ObjectNode createOrderJsonArray(List<Order> orders){
        ObjectMapper mapper = new ObjectMapper();

        ArrayNode jsOrders = mapper.createArrayNode();

        for (Order order: orders) {

            String userId = order.getUser() == null ? "" : order.getUser().getId1c();
            String id1c = order.getId1c() == null ? "" : order.getId1c();

            ObjectNode jsOrder = mapper.createObjectNode();
            jsOrder.put("id", order.getId().toString());
            jsOrder.put("date", order.getDate().toString());
            jsOrder.put("user", userId);
            jsOrder.put("id1c", id1c);

            List<OrderLine> lines = order.getLines();
            ArrayNode jsLines = mapper.createArrayNode();

            for (OrderLine ol: lines) {
                ObjectNode jsLine = mapper.createObjectNode();
                jsLine.put("productId", ol.getProduct().getId1c());
                jsLine.put("characId", ol.getCharac().getId1c());
                jsLine.put("count", ol.getCount());
                jsLine.put("price", ol.getPrice());
                jsLine.put("sum", ol.getSum());
                jsLines.add(jsLine);
            }

            jsOrder.put("lines", jsLines);
            jsOrders.add(jsOrder);
        }

        ObjectNode response = mapper.createObjectNode();
        response.put("response", jsOrders);
        return response;
    }
    //endregion
}
