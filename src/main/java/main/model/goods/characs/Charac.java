package main.model.goods.characs;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import main.model.goods.Product;
import main.model.images.Image;
import main.model.goods.characs.characproperty.CharacPropertyValue;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "characs")
public class Charac{
    //region FIELDS
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private String id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private Product product;

    private String name;

    private String id1c;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "charac_id")
    private List<CharacPropertyValue> propertyes;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "charac_id")
    private List<Image> images;

    private BigDecimal price;

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