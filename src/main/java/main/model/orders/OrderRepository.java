package main.model.orders;

import org.aspectj.weaver.ast.Or;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Long> {

    Order findById1c(String id1c);

    Order findById(long id);

}