package main.model;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostAdressRepository extends CrudRepository<PostAdress, Integer> {
    List<PostAdress> findByUser(User user);

    PostAdress findByPostAdress(String postAdress);
}
