package main.model.user.adress;

import main.model.user.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostAdressRepository extends CrudRepository<PostAdress, Integer> {
    List<PostAdress> findByUser(User user);

    PostAdress findByPostAdress(String postAdress);
}
