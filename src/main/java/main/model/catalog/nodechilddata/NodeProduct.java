package main.model.catalog.nodechilddata;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import main.model.catalog.CatalogNode;
import main.model.goods.Product;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "node_products", indexes = {
        @Index(name = "idx_nodeproduct_node_id", columnList = "node_id, product_id")
})
public class NodeProduct {
    //region FIELDS
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private String id;

    @ManyToOne
    @JoinColumn(name = "node_id")
    private CatalogNode node;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    //endregion
}
