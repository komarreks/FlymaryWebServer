package main.model.propertyes;

import main.model.goods.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface GoodPropertyValueRepository extends CrudRepository<GoodPropertyValue, UUID> {

    GoodPropertyValue findByProductAndProperty(Product product, Property property);

}
