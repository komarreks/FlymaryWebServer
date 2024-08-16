package main.model.orders;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import main.model.goods.Product;
import main.model.goods.characs.Charac;

@Getter
@Setter
@Entity
@IdClass(OrderLinePK.class)
@Table(name = "order_lines")
public class OrderLine {

    @Id
    private int lineNumber;
    @Id
    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "charac_id")
    private Charac charac;

    private int count;

    private double price;

    private double sum;
}