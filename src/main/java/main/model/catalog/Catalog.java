package main.model.catalog;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "catalogs", indexes = {
        @Index(name = "idx_catalog_id_id1c_unq", columnList = "id, id1c", unique = true)
})
public class Catalog {
    //region FIELDS
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    private String id1c;

    private String name;

    private String textButton;

    private String imagePath;

    private int version;

    private int deleted;

    private List<String> goodProperty;

    private List<String> characProperty;
    //endregion

    //region METHODS
    public void loadGoodPropertyes(List<String> list){
        if (goodProperty == null){
            goodProperty = new ArrayList<>();
        }
        goodProperty.clear();
        goodProperty.addAll(list);
    }

    public void loadcharacPropertyes(List<String> list){
        if (characProperty == null){
            characProperty = new ArrayList<>();
        }
        characProperty.clear();
        characProperty.addAll(list);
    }
    //endregion
}