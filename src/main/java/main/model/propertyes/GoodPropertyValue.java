package main.model.propertyes;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import main.model.goods.Product;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class GoodPropertyValue {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "property_id")
    private Property property;

    private String value;

    public GoodPropertyValue(Product product, Property property, String value) {
        this.product = product;
        this.property = property;
        this.value = value;
    }
}
