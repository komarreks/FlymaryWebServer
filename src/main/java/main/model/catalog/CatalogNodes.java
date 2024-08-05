package main.model.catalog;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "nodes")
public class CatalogNodes {
    @Id
    @Column(name = "id", nullable = false)
    private int id;

    private String id1c;

    private String name;

    private int sorting;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private CatalogNodes parent;

    @ManyToOne
    @JoinColumn(name = "catalog_id")
    private Catalog catalog;

    private int version;

    private String imagePath;
}
