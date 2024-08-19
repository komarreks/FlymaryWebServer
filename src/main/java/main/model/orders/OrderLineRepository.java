package main.model.orders;

import org.aspectj.weaver.ast.Or;
import org.springframework.data.repository.CrudRepository;

public interface OrderLineRepository extends CrudRepository<OrderLine, OrderLinePK> {

    OrderLine findByLineNumberAndOrder(int lineNumber, Order order);

}
