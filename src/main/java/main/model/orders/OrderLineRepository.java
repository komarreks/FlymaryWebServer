package main.model.orders;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface OrderLineRepository extends CrudRepository<OrderLine, OrderLinePK> {

    @Transactional
    @Modifying
    @Query("delete from OrderLine o where o.orderLinePK.order=:order")
    void deleteAllByOrder(Order order);

    @Transactional
    @Modifying
    @Query("delete from OrderLine o where o.deleted=1")
    void deleteAllByDeleted();
}
