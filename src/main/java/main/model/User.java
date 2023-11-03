package main.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    String id1c;
    String name;
    String phone;

    @Column(name = "post_adres")
    String postAdres;
}
