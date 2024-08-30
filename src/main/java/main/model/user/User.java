package main.model.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import main.model.user.phone.Phone;
import main.model.user.adress.PostAdress;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Entity
@Getter
@Setter
@Table(name = "users", indexes = {
        @Index(name = "idx_user_id_id1c_unq", columnList = "id, id1c", unique = true)
})
public class User {
    //region FIELDS
    @Id
    private UUID id;
    private String id1c;
    private String name;
    private int deleted;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Phone> phones;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PostAdress> postAdresses;
    //endregion

    //region CONSTRUCTORS
    public User(){
        phones = new ArrayList<>();
        postAdresses = new ArrayList<>();
    }
    //endregion

    //region METHODS
    public void deletePhone(Phone phone){
        phones.remove(phone);
    }

    public void clearPhonesAndPostAdress(){
        if (phones == null) phones = new ArrayList<>();
        phones.clear();

        if (postAdresses == null) postAdresses = new ArrayList<>();
        postAdresses.clear();
    }

    public void savePhonesAndPostAdress(List<String> phones, List<String> postAdresses){
        for (String strPhone: phones) {
            Phone phone = new Phone(this, strPhone);
            this.phones.add(phone);
        }

        for (String strAdress: postAdresses) {
            PostAdress postAdress = new PostAdress(this, strAdress);
            this.postAdresses.add(postAdress);
        }
    }

    public void deletePostAdress(PostAdress postAdress){
        postAdresses.remove(postAdress);
    }

    public String phonesToString(){
        StringBuilder sb = new StringBuilder("");
        AtomicReference<String> appendix = new AtomicReference<>("");
        phones.forEach(phone -> {
            sb.append(phone.getPhone() + appendix.get());
            appendix.set("; ");
        });
        return sb.toString();
    }

    public String adressesToString(){
        StringBuilder sb = new StringBuilder("");
        AtomicReference<String> appendix = new AtomicReference<>("");
        postAdresses.forEach(adress -> {
            sb.append(adress.getPostAdress() + appendix.get());
            appendix.set("; ");
        });
        return sb.toString();
    }
    //endregion
}
