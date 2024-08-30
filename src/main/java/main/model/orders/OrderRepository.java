package main.model.orders;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    Order findById1c(String id1c);

    Optional<Order> findById(UUID id);

    List<Order> findByStatus(OrderStatus orderStatus);
}