package main.model.orders;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import main.model.goods.Product;
import main.model.goods.characs.Charac;

@Getter
@Setter
@Entity
@Table(name = "order_lines")
public class OrderLine {

    @EmbeddedId
    OrderLinePK orderLinePK;

    @ManyToOne()
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne()
    @JoinColumn(name = "charac_id")
    private Charac charac;

    private int count;

    private double price;

    private double sum;

    private int deleted;
}