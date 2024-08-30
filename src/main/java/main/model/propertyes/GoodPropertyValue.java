package main.model.propertyes;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import main.model.goods.Product;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "good_property_values", indexes = {
        @Index(name = "idx_goodpropertyvalue", columnList = "product_id, property_id")
})
@NoArgsConstructor
public class GoodPropertyValue {
    // region FIELDS
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "property_id")
    private Property property;

    private String value;
    //endregion

    //region CONSTRUCTORS
    public GoodPropertyValue(Product product, Property property, String value) {
        id = UUID.randomUUID();
        this.product = product;
        this.property = property;
        this.value = value;
    }
    //endregion
}
