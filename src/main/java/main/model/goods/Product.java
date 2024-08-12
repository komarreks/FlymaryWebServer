package main.model.goods;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import main.model.images.Image;
import main.model.propertyes.GoodPropertyValue;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Product {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String id1c;

    private String name;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private List<GoodPropertyValue> propertyes;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private List<Image> images;

    public void clearPropertyes(){
        if (propertyes == null) propertyes = new ArrayList<>();
        propertyes.clear();
    }

    public void addProperty(GoodPropertyValue property){
        propertyes.add(property);
    }
}
