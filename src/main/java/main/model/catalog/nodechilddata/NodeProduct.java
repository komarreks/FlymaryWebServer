package main.model.catalog.nodechilddata;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import main.model.catalog.CatalogNode;
import main.model.goods.Product;

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
    UUID id;

    @ManyToOne
    @JoinColumn(name = "node_id")
    CatalogNode node;

    @ManyToOne
    @JoinColumn(name = "product_id")
    Product product;
    //endregion
}
