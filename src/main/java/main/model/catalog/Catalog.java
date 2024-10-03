package main.model.catalog;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.Base64;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "catalogs", indexes = {
        @Index(name = "idx_catalog_id", columnList = "id"),
        @Index(name = "idx_catalog_id1c", columnList = "id1c")
})
public class Catalog {
    //region FIELDS
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private String id;

    private String id1c;

    private String name;

    private String textButton;

    private int sorting;

    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private byte[] image64;

    private int version;

    private int deleted;

    private List<String> goodProperty;

    private List<String> characProperty;
    //endregion
}