package main.transfer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import main.answers.SimpleAnswer;
import main.answers.StatusLoad;
import main.model.goods.Product;
import main.model.goods.ProductReposytory;
import main.model.goods.characs.Charac;
import main.model.goods.characs.CharacRepository;
import main.model.orders.*;
import main.model.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrdersController {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProductReposytory productReposytory;
    @Autowired
    CharacRepository characRepository;
    @Autowired
    OrderLineRepository orderLineRepository;

    @PostMapping(value = "/updateOrders", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public void updateOrders(@RequestBody ArrayNode ordersList){
        StatusLoad statusLoad = new StatusLoad();
        statusLoad.setStatus(true);

        ordersList.forEach(jsonNode -> {
            String id1c = jsonNode.get("id1c").textValue();

            Order order = orderRepository.findById1c(id1c);

            if (order == null){
                long id = jsonNode.get("site_id").intValue();

                order = orderRepository.findById(id);

                if (order != null){
                    order.setId1c(id1c);
                }
            }

            if (order == null){
                statusLoad.addLog(id1c, "Заказ не синхронизирован");
            }

            if (order != null){
                //order.setDate(jsonNode.get("date"));
                order.setStatus(OrderStatus.getStatus(jsonNode.get("status").textValue()));
                order.setUser(userRepository.findById1c(jsonNode.get("userId1c").textValue()));

                order.clearTable();

                ArrayNode jsLines = (ArrayNode) jsonNode.get("orderLines");

                for (JsonNode line : jsLines) {
                    Product product = productReposytory.findById1c(line.get("productId1c").textValue());
                    Charac charac = characRepository.findById1c(line.get("characId1c").textValue());
                    int count = line.get("count").intValue();
                    double price = line.get("price").doubleValue();

                    order.addLine(product, charac, count, price);
                }

                orderRepository.save(order);

                statusLoad.addLog(id1c, "Успешно обновлен");
            }
        });
    }

    @GetMapping(value = "/createOrder")
    public ResponseEntity createOrder(){
        Order order = Order.createNewOrder();

        orderRepository.save(order);

        return ResponseEntity.status(HttpStatus.CREATED).body(order.getId());
    }

    @GetMapping(value = "/createOrderWithAutorize")
    public ResponseEntity createOrder(@RequestBody JsonNode jsUserId){
        Order order = Order.createNewOrder();

        order.setUser(userRepository.findById(jsUserId.get("userId").intValue()).get());

        orderRepository.save(order);

        return ResponseEntity.status(HttpStatus.CREATED).body(order.getId());
    }

    @PostMapping(value = "/addLine", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity addLine(@RequestBody JsonNode jsLine){
        Order order = orderRepository.findById(jsLine.get("id").longValue());

        if (order == null){return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SimpleAnswer("not found order"));}

        Product product = productReposytory.findById(jsLine.get("productId").longValue());

        if (product == null){return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SimpleAnswer("not found product"));}

        long characId = jsLine.get("characId").longValue();

        Charac charac = null;

        if (characId > 0){
            charac = characRepository.findById(characId);

            if (charac == null){return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SimpleAnswer("not found charac"));}
        }

        int count = jsLine.get("count").intValue();
        double price = jsLine.get("price").doubleValue();

        order.addLine(product, charac, count, price);

        orderRepository.save(order);

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping(value = "/changeCount", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity changeCount(@RequestBody JsonNode jsLine){
        long id = jsLine.get("id").longValue();

        Order order = orderRepository.findById(id);

        if (order == null){return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SimpleAnswer("not found order"));}

        int lineNumber = jsLine.get("lineNumber").intValue();
        int newCount = jsLine.get("newCount").intValue();

        order.changeCount(lineNumber, newCount);

        orderRepository.save(order);

        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping(value = "/deleteLine")
    public ResponseEntity deleteLine(@RequestBody JsonNode jsLine){
        long id = jsLine.get("id").longValue();

        Order order = orderRepository.findById(id);

        if (order == null){return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SimpleAnswer("not found order"));}

        order.setOrderLineRepository(orderLineRepository);
        order.setOrderRepository(orderRepository);
        order.deleteLine(jsLine.get("lineNumber").intValue());

        orderRepository.save(order);
        orderLineRepository.deleteAllByDeleted();

        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping(value = "/deleteOrder")
    public ResponseEntity deleteOrder(@RequestBody JsonNode jsLine){
        Order order = orderRepository.findById(jsLine.get("id").longValue());

        if (order == null) return new ResponseEntity(HttpStatus.NOT_FOUND);

        if (order.getId1c() != null){
            if (!order.getId1c().isEmpty()) return new ResponseEntity("order follow to 1c, sorry", HttpStatus.CONFLICT);
        }

        orderLineRepository.deleteAllByOrder(order);
        orderRepository.delete(order);

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(value = "getOpenOrders")
    public ResponseEntity getOpenOrders(){
        List<Order> openedOrders = orderRepository.findByStatus(OrderStatus.OPEN);

        ObjectMapper mapper = new ObjectMapper();

        ArrayNode jsOrders = mapper.createArrayNode();

        for (Order order: openedOrders) {

            String userId = order.getUser() == null ? "":order.getUser().getId1c();

            ObjectNode jsOrder = mapper.createObjectNode();
            jsOrder.put("id", order.getId());
            jsOrder.put("date", order.getDate().toString());
            jsOrder.put("user", userId);

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

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
