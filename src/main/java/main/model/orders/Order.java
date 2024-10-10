package main.model.orders;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import main.model.goods.Product;
import main.model.goods.characs.Charac;
import main.model.user.User;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {
    //region FIELDS
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private String id;

    private LocalDateTime date;
    private LocalDateTime dateOpenOrder;

    @Enumerated
    @Column(name = "status")
    private OrderStatus status;

    private String id1c;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private List<OrderLine> lines;
    //endregion

    //region METHODS
    public static Order createNewOrder(){
        Order order = new Order();
        order.setDate(LocalDateTime.now());
        order.setStatus(OrderStatus.OPEN);
        order.clearTable();

        return order;
    }

    public void clearTable(){
        lines = new ArrayList<>();
    }

    public void addLine(Product product, Charac charac, int count, double price){
        OrderLine orderLine = new OrderLine();
        orderLine.setOrderLinePK(new OrderLinePK(lines.size() + 1, this));
        orderLine.setProduct(product);
        orderLine.setCharac(charac);
        orderLine.setCount(count);
        orderLine.setPrice(price);
        orderLine.setSum(count * price);

        lines.add(orderLine);
    }

    public List<OrderLine> getLines(){
        return lines;
    }

    public void deleteLine(int number){
        List<OrderLine> buffer = new ArrayList<>();
        buffer.addAll(lines);

        AtomicInteger numberLine = new AtomicInteger(0);
        buffer = buffer.stream()
                .filter(line -> line.getOrderLinePK().getLineNumber() != number).toList();

        buffer.forEach(buferLine -> {
            OrderLine ol = lines.get(numberLine.get());
            ol.setProduct(buferLine.getProduct());
            ol.setCharac(buferLine.getCharac());
            ol.setCount(buferLine.getCount());
            ol.setPrice(buferLine.getPrice());
            ol.setSum(buferLine.getSum());
            numberLine.incrementAndGet();
        });

        OrderLine deletedLine = lines.get(lines.size() -1);
        deletedLine.setDeleted(1);
    }

    public void changeCount(int lineNumber, int newCount){
        for (OrderLine line: lines) {
            if (line.getOrderLinePK().getLineNumber() == lineNumber){
                line.setCount(newCount);
                line.setSum(line.getCount() * line.getPrice());
            }
        }
    }
    //endregion
}