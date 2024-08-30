package main.model.propertyes;

import main.model.goods.characs.Charac;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface CharacPropertyValueRepository extends CrudRepository<CharacPropertyValue, UUID> {

    CharacPropertyValue findByCharacAndProperty(Charac charac, Property property);

}
