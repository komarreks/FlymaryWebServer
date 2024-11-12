package main.model.images;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import main.model.goods.Product;
import main.model.goods.characs.Charac;
import main.model.viewfeatures.banners.Banner;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "images")
@Getter
@Setter
public class Image {
    //region FIELDS
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private String id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "charac_id")
    private Charac charac;

    @ManyToOne
    @JoinColumn(name = "banner_id")
    private Banner banner;

    private String id1c;

    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private byte[] image64;

    private String name;

    private int deleted;
    //endregion
}
