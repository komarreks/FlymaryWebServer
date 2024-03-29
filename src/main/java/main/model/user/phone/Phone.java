package main.model.user.phone;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import main.model.user.User;

@Entity
@Getter
@Setter
@Table(name = "phones")
public class Phone {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;
    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "user_id")
    User user;
    String phone;

    public Phone(User user, String phone){
        this.phone = phone;
        this.user = user;
    }

    public Phone(){
        user = null;
        phone = "";
    }
}
