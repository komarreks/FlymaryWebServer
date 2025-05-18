package main.model.orders;

import main.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, String> {

    Order findById1c(String id1c);

    Optional<Order> findById(String id);

    List<Order> findByStatus(OrderStatus orderStatus);

    List<Order> findByStatusAndUser(OrderStatus orderStatus, User user);
}