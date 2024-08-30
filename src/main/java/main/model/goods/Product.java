package main.model.goods;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import main.model.images.Image;
import main.model.propertyes.GoodPropertyValue;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "products", indexes = {
        @Index(name = "idx_product_id_id1c", columnList = "id, id1c")
})
public class Product {
    //region FIELDS
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    private String id1c;

    private String name;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private List<GoodPropertyValue> propertyes;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private List<Image> images;
    //endregion

    //region METHODS
    public void clearPropertyes(){
        if (propertyes == null) propertyes = new ArrayList<>();
        propertyes.clear();
    }

    public void addProperty(GoodPropertyValue property){
        propertyes.add(property);
    }
    //endregion
}
