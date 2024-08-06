package main.model.propertyes;

import main.model.goods.Product;
import org.springframework.data.repository.CrudRepository;

public interface GoodPropertyValueRepository extends CrudRepository<GoodPropertyValue, Integer> {

    GoodPropertyValue findByProductAndProperty(Product product, Property property);
}
