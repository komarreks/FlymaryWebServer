package main.model.user.phone;

import main.model.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhonesRepository extends CrudRepository<Phone, Integer> {

    Phone findByPhone(String phone);

    List<Phone> findByUser(User user);
}
