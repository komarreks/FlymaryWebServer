package main.model.orders;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Order findById1c(String id1c);

    Order findById(long id);
}