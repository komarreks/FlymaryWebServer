package main.model.catalog;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "nodes", indexes = {
        @Index(name = "idx_catalognodes_id_id1c", columnList = "id, id1c, parent_id, catalog_id")
})
public class CatalogNodes {
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
    private CatalogNodes parent;

    @ManyToOne
    @JoinColumn(name = "catalog_id")
    private Catalog catalog;

    private int version;

    private String imagePath;
    //endregion
}
