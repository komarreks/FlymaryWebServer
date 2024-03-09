package main.model.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import main.model.user.phone.Phone;
import main.model.user.adress.PostAdress;

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
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PostAdress> postAdresses;

    public User(){
        id1c = "";
        name = "";
        phones = new ArrayList<>();
        postAdresses = new ArrayList<>();
    }

    public void deletePhone(Phone phone){
        phones.remove(phone);
    }

    public void deletePostAdress(PostAdress postAdress){
        postAdresses.remove(postAdress);
    }
}
