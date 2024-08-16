package main.model.orders;

import org.springframework.data.repository.CrudRepository;

public interface OrderLineRepository extends CrudRepository<OrderLine, OrderLinePK> {
}
