package main.model.images;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import main.model.goods.Product;
import main.model.goods.characs.Charac;

import java.util.UUID;

@Entity
@Table(name = "images")
@Getter
@Setter
public class Image {
    //region FIELDS
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "charac_id")
    private Charac charac;

    private String id1c;

    private String path;

    private String name;

    private int deleted;
    //endregion
}
