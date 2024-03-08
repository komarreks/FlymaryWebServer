package main.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "post_aderesses")
public class PostAdress {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;

    private String postAdress;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "user_id")
    private User user;

    public PostAdress(){
        postAdress = "";
        user = null;
    }

    public PostAdress(User user, String postAdress){
        this.user = user;
        this.postAdress = postAdress;
    }
}
