package main.model.user.adress;

import main.model.user.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface PostAdressRepository extends CrudRepository<PostAdress, UUID> {
    List<PostAdress> findByUser(User user);

    PostAdress findByPostAdress(String postAdress);
}
