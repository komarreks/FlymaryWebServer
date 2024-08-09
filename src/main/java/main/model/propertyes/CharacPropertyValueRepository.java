package main.model.propertyes;

import main.model.goods.characs.Charac;
import org.springframework.data.repository.CrudRepository;

public interface CharacPropertyValueRepository extends CrudRepository<CharacPropertyValue, Long> {

    CharacPropertyValue findByCharacAndProperty(Charac charac, Property property);

}
