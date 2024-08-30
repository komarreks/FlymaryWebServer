package main.model.user.phone;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import main.model.user.User;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "phones", indexes = {
        @Index(name = "idx_phone_id_user_id", columnList = "id, user_id")
})
@NoArgsConstructor
public class Phone {
    //region FIELDS
    @Id
    UUID id;
    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "user_id")
    User user;
    String phone;
    //endregion

    //region CONSTRUCTORS
    public Phone(User user, String phone){
        id = UUID.randomUUID();
        this.phone = phone;
        this.user = user;
    }
    //endregion
}
