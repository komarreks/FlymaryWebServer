package main.model.propertyes.goodpropery;

import main.model.goods.Product;
import main.model.propertyes.Property;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoodPropertyValueRepository extends JpaRepository<GoodPropertyValue, GoodPropertyPK> {

    //GoodPropertyValue findByProductAndProperty(Product product, Property property);

}
