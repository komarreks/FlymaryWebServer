package main.model.propertyes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "propertyes")
public class Property {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    private String id1c;

    private String name;
}
