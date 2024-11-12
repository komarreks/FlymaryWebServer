package main.model.orders;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import main.model.goods.Product;
import main.model.goods.characs.Charac;

import java.math.BigDecimal;

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

    private BigDecimal count;

    private BigDecimal price;

    private BigDecimal sum;

    private int deleted;
}