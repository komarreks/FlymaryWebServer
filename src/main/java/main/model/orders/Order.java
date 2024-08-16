package main.model.orders;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import main.model.goods.Product;
import main.model.goods.characs.Charac;
import main.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {
    @Autowired
    @Transient
    OrderLineRepository orderLineRepository;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private LocalDateTime date;

    @Enumerated
    @Column(name = "status")
    private OrderStatus status;

    private String id1c;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private List<OrderLine> lines;

    public void clearTable(){
        orderLineRepository.deleteAll(lines);
    }

    public void addLine(Product product, Charac charac, int count, double price){
        OrderLine orderLine = new OrderLine();
        orderLine.setLineNumber(lines.size() + 1);
        orderLine.setOrder(this);
        orderLine.setProduct(product);
        orderLine.setCharac(charac);
        orderLine.setCount(count);
        orderLine.setPrice(price);
        orderLine.setSum(count * price);

        lines.add(orderLine);
    }

    public void deleteLine(int number){
        orderLineRepository.delete(lines.get(number - 1));

        number = 1;
        for (OrderLine line: lines) {
            line.setLineNumber(number);
            number++;
        }
    }
}