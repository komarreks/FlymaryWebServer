package main.model.user.adress;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import main.model.user.User;

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
