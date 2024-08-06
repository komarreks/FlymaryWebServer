package main.model.goods;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import main.model.propertyes.GoodPropertyes;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Product {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    private String id1c;

    private String name;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private List<GoodPropertyes> propertyes;

    public void clearPropertyes(){
        if (propertyes == null) propertyes = new ArrayList<>();
        propertyes.clear();
    }

    public void addProperty(GoodPropertyes property){
        propertyes.add(property);
    }
}
