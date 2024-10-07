package main.model.goods.goodpropery;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GoodPropertyValueRepository extends JpaRepository<GoodPropertyValue, GoodPropertyPK> {

    //GoodPropertyValue findByProductAndProperty(Product product, Property property);

}
