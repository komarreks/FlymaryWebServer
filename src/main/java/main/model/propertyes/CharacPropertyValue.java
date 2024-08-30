package main.model.propertyes;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import main.model.goods.characs.Charac;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "charac_property_values", indexes = {
        @Index(name = "idx_characpropertyvalue", columnList = "charac_id, property_id")
})
@NoArgsConstructor
public class CharacPropertyValue {
    //region FIELDS
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "charac_id")
    private Charac charac;

    @ManyToOne
    @JoinColumn(name = "property_id")
    private Property property;

    private String value;
    //endregion

    //region CONSTRUCTORS
    public CharacPropertyValue(Charac charac, Property property, String value) {
        id = UUID.randomUUID();
        this.charac = charac;
        this.property = property;
        this.value = value;
    }
    //endregion
}
