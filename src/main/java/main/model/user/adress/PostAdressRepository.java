package main.model.user.adress;

import main.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface PostAdressRepository extends JpaRepository<PostAdress, String> {
    List<PostAdress> findByUser(User user);

    PostAdress findByPostAdress(String postAdress);
}
