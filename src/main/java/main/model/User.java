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
    private String id1c;
    private String name;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Phone> phones;
    @Column(name = "post_adress")
    private String postAdress;

    public User(){
        id1c = "";
        name = "";
        phones = new ArrayList<>();
        postAdress = "";
    }

    public void deletePhone(Phone phone){
//        int index = phones.indexOf(phone);
        phones.remove(phone);
    }
}
