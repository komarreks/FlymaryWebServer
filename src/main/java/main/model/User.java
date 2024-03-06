package main.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    String id1c;
    String name;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<Phone> phones;
    @Column(name = "post_adress")
    String postAdress;

    public User(){
        id1c = "";
        name = "";
        phones = new ArrayList<>();
        postAdress = "";
    }
}
