package main.model.orders;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Order findById1c(String id1c);

    Order findById(long id);

    List<Order> findByStatus(OrderStatus orderStatus);
}