package main.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.RequiredArgsConstructor;
import main.answers.LoadLine;
import main.answers.SimpleAnswer;
import main.answers.StatusLoad;
import main.model.goods.Product;
import main.model.goods.characs.Charac;
import main.model.orders.Order;
import main.model.orders.OrderLineRepository;
import main.model.orders.OrderRepository;
import main.model.orders.OrderStatus;
import main.model.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductSevice productSevice;
    private final OrderLineRepository orderLineRepository;

    public StatusLoad loadChangedOrders(ArrayNode jsOrders){
        //TODO доделать загрузку изменений заказа из 1с
        StatusLoad statusLoad = new StatusLoad();

        jsOrders.forEach(jsOrder -> {
            String id1c = jsOrder.get("id1c").textValue();

            LoadLine loadLine = new LoadLine(id1c);

            Order order = orderRepository.findById1c(id1c);

            if (order == null){
                String id = jsOrder.get("site_id").textValue();

                Optional<Order> optionalOrder = orderRepository.findById(id);

                if (optionalOrder.isPresent()){
                    order = optionalOrder.get();
                    order.setId1c(id1c);
                }
            }

            if (order == null){
                loadLine.setStatus("Заказ не синхронизирован");
                statusLoad.addLog(loadLine);
            }

            if (order != null){
                //order.setDate(jsOrder.get("date"));
                order.setStatus(OrderStatus.getStatus(jsOrder.get("status").textValue()));
                order.setUser(userRepository.findById1c(jsOrder.get("userId1c").textValue()));

                order.clearTable();

                ArrayNode jsLines = (ArrayNode) jsOrder.get("orderLines");

                for (JsonNode line : jsLines) {
                    Product product = productSevice.findById1c(line.get("productId1c").textValue());
                    Charac charac = productSevice.findCharacById1c(line.get("characId1c").textValue());
                    int count = line.get("count").intValue();
                    double price = line.get("price").doubleValue();

                    order.addLine(product, charac, count, price);
                }

                orderRepository.save(order);

                loadLine.setStatus("Успешно обновлен");

                statusLoad.addLog(loadLine);
            }
        });

        return statusLoad;
    }

    public Order createNewOrder(JsonNode jsUserId){
        Order order = Order.createNewOrder();

        //FIXME после изменения сущности покупателя изменить код тут
        //User user = userRepository.findById(jsUserId.get("userId").textValue());

        //order.setUser(user);

        orderRepository.save(order);

        return order;
    }

    public SimpleAnswer addLine(JsonNode jsOrderLine){
        Order order = orderRepository.findById(jsOrderLine.get("id").textValue()).orElse(null);

        if (order == null){return new SimpleAnswer(true, "not found order");}

        Product product = productSevice.findById(jsOrderLine.get("productId").textValue());

        if (product == null){return new SimpleAnswer(true,"not found product");}

        String characId = jsOrderLine.get("characId").textValue();

        Charac charac = null;

        if (characId.length() > 0){
            charac = productSevice.findCharacById1c(characId);

            if (charac == null){return new SimpleAnswer(true,"not found charac");}
        }

        int count = jsOrderLine.get("count").intValue();
        double price = jsOrderLine.get("price").doubleValue();

        order.addLine(product, charac, count, price);

        orderRepository.save(order);

        return new SimpleAnswer(false, "success");
    }

    public SimpleAnswer changeCountInLine(JsonNode jsOrderLine){
        String id = jsOrderLine.get("id").textValue();

        Order order = orderRepository.findById(id).orElse(null);

        if (order == null){return new SimpleAnswer(true,"not found order");}

        int lineNumber = jsOrderLine.get("lineNumber").intValue();
        int newCount = jsOrderLine.get("newCount").intValue();

        order.changeCount(lineNumber, newCount);

        orderRepository.save(order);

        return new SimpleAnswer(false, "succeed");
    }

    public SimpleAnswer deleteLine(JsonNode jsOrderLine){
        String id = jsOrderLine.get("id").textValue();

        Order order = orderRepository.findById(id).orElse(null);

        if (order == null){return new SimpleAnswer(true, "not found order");}

        order.deleteLine(jsOrderLine.get("lineNumber").intValue());

        orderRepository.save(order);
        orderLineRepository.deleteAllByDeleted();

        return new SimpleAnswer(false, "success");
    }

    public SimpleAnswer deleteOrder(JsonNode jsOrder){
        Order order = orderRepository.findById(jsOrder.get("id").textValue()).orElse(null);

        if (order == null) return new SimpleAnswer(true, "order not found");

        if (order.getId1c() != null){
            if (!order.getId1c().isEmpty()) return new SimpleAnswer(true, "order follow to 1c, sorry");
        }

        orderLineRepository.deleteAllByOrder(order);
        orderRepository.delete(order);

        return new SimpleAnswer(false, "success");
    }

    public SimpleAnswer finallyOrder(JsonNode jsOrder){
        Order order = orderRepository.findById(jsOrder.get("id").textValue()).orElse(null);

        if (order==null) return new SimpleAnswer(true, "order not found");

        order.setStatus(OrderStatus.IN_WORK);
        order.setDateOpenOrder(order.getDate());
        order.setDate(LocalDateTime.now());

        orderRepository.save(order);
        return new SimpleAnswer(true, "success");

    }

    public List<Order> findByStatus(OrderStatus orderStatus) {
        return orderRepository.findByStatus(orderStatus);
    }
}
