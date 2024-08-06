package main.model.propertyes;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import main.model.goods.Product;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class GoodPropertyes {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private String name;

    private String value;

    public GoodPropertyes(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
