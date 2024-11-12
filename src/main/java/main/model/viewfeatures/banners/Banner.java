package main.model.viewfeatures.banners;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import main.model.images.Image;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "banners")
public class Banner {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private String id;

    private String id1c;

    private String name;

    private int version;

    private int active;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "banner_id")
    private List<Image> images;
}
