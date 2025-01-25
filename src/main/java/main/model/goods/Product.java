package main.model.goods;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import main.model.images.Image;
import main.model.goods.goodpropery.GoodPropertyValue;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "products", indexes = {
        @Index(name = "idx_product_id", columnList = "id"),
        @Index(name = "idx_product_id1c", columnList = "id1c")
})
public class Product {
    //region FIELDS
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private String id;

    private String id1c;

    private String name;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private List<GoodPropertyValue> propertyes;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private List<Image> images;

    private BigDecimal price;
    //endregion

    //region METHODS
    public void clearPropertyes(){
        if (propertyes == null) propertyes = new ArrayList<>();
        propertyes.clear();
    }

    public void addProperty(GoodPropertyValue property){
        propertyes.add(property);
    }

    public <T> Object getProperty(String property){
        for (GoodPropertyValue propertyValue : propertyes) {
            if (propertyValue.getGoodPropertyPK().getProperty().equals(property)) {
                return propertyValue.getValue();
            }
        }
        return "";
    }

    public boolean isVisible(){
        Object value = getProperty("notVisible");

        Boolean bool = (Boolean) value;

        return !bool;
    }
    //endregion
}
