package main.model.catalog;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import main.model.catalog.nodechilddata.NodeProduct;
import main.model.goods.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "nodes", indexes = {
        @Index(name = "idx_catalognodes_id_id1c", columnList = "id, id1c, parent_id, catalog_id")
})
public class CatalogNode {
    //region FIELDS
    @Id
    @Column(name = "id", nullable = false)
    private int id;

    private String id1c;

    private String name;

    private int sorting;

    private int deleted;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private CatalogNode parent;

    @ManyToOne
    @JoinColumn(name = "catalog_id")
    private Catalog catalog;

    private int version;

    private String imagePath;

    @OneToMany(mappedBy = "node", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NodeProduct> products = new ArrayList<>();

//endregion

    //region METHODS
    public void addProduct(Product product){
        NodeProduct nodeProduct = new NodeProduct();
        nodeProduct.setId(UUID.randomUUID());
        nodeProduct.setNode(this);
        nodeProduct.setProduct(product);
        products.add(nodeProduct);
    }
    //endregion
}
