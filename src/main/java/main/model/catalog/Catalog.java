package main.model.catalog;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "catalogs")
public class Catalog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;

    private String id1c;

    private String name;

    private String textButton;

    private String imagePath;

    private int version;

    private List<String> goodProperty;

    private List<String> characProperty;

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
}