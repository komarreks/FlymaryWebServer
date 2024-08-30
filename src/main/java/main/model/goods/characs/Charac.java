package main.model.goods.characs;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import main.model.goods.Product;
import main.model.images.Image;
import main.model.propertyes.CharacPropertyValue;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "characs")
public class Charac{
    //region FIELDS
    @Id
    @Column(nullable = false)
    private UUID id;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private String name;

    private String id1c;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "charac_id")
    private List<CharacPropertyValue> propertyes;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "charac_id")
    private List<Image> images;
    //endregion

    //region METHODS
    public void clearPropertyes() {
        if (propertyes == null) propertyes = new ArrayList<>();
        propertyes.clear();
    }

    public void addProperty(CharacPropertyValue newProperty) {
        propertyes.add(newProperty);
    }
    //endregion
}