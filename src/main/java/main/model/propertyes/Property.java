package main.model.propertyes;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "propertyes", indexes = {
        @Index(name = "idx_property_id_id1c_unq", columnList = "id, id1c", unique = true)
})
public class Property {
    //region FIELDS
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    private String id1c;

    private String name;
    //endregion
}
