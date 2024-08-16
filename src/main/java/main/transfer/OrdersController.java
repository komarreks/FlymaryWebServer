package main.transfer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.mysql.cj.xdevapi.JsonArray;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;

@Controller
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
        Order order = new Order();
        order.setDate(LocalDateTime.now());
        order.setStatus(OrderStatus.OPEN);

        orderRepository.save(order);

        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }


}
