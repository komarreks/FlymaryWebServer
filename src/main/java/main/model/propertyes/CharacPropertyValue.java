package main.model.propertyes;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import main.model.goods.characs.Charac;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class CharacPropertyValue {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "charac_id")
    private Charac charac;

    @ManyToOne
    @JoinColumn(name = "property_id")
    private Property property;

    private String value;

    public CharacPropertyValue(Charac charac, Property property, String value) {
        this.charac = charac;
        this.property = property;
        this.value = value;
    }
}
