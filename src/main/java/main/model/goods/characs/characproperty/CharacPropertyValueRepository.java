package main.model.goods.characs.characproperty;

import main.model.goods.characs.Charac;
import main.model.propertyes.Property;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface CharacPropertyValueRepository extends CrudRepository<CharacPropertyValue, String> {

    //CharacPropertyValue findByCharacAndProperty(Charac charac, Property property);

}
