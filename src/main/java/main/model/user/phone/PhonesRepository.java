package main.model.user.phone;

import main.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PhonesRepository extends JpaRepository<Phone, String> {

    Phone findByPhone(String phone);

    List<Phone> findByUser(User user);
}
