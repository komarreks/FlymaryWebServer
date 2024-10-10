package main.model.user.adress;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import main.model.user.User;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Setter
@Getter
@Table(name = "post_aderesses", indexes = {
        @Index(name = "idx_postadress_id", columnList = "id")
})
@NoArgsConstructor
public class PostAdress {
    //region FIELDS
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    String id;

    private String postAdress;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "user_id")
    private User user;
    //endregion

    //region CONSTRUCTORS
    public PostAdress(User user, String postAdress){
        this.user = user;
        this.postAdress = postAdress;
    }
    //endregion
}
