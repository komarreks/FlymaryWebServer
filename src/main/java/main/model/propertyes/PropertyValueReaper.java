package main.model.propertyes;

import main.model.goods.Product;
import main.model.goods.characs.Charac;
import main.model.goods.characs.characproperty.CharacPropertyValue;
import main.model.goods.characs.characproperty.CharacPropertyValueRepository;
import main.model.goods.goodpropery.GoodPropertyValue;
import main.model.goods.goodpropery.GoodPropertyValueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PropertyValueReaper {

    @Autowired
    GoodPropertyValueRepository goodPropertyValueRepository;
    @Autowired
    PropertyReposytory propertyReposytory;
    @Autowired
    CharacPropertyValueRepository characPropertyValueRepository;

    public GoodPropertyValue findPropertyValue(Product product, String name, String value){
        Property property = propertyReposytory.findByName(name);

        GoodPropertyValue goodPropertyValue = null;

//        if (!(product.getId() == null)){
//            goodPropertyValue = goodPropertyValueRepository.findByProductAndProperty(product, property);
//        }

//        if (goodPropertyValue == null){
//            goodPropertyValue = new GoodPropertyValue(product, property, value);
//        }

//        if (!goodPropertyValue.getValue().equals(value)){
//            goodPropertyValue.setValue(value);
//        }

        return goodPropertyValue;
    }

    public CharacPropertyValue findPropertyValue(Charac charac, String name, String value){
        Property property = propertyReposytory.findByName(name);

        CharacPropertyValue characPropertyValue = null;

//        if (!(charac.getId() == null)){
//            characPropertyValue = characPropertyValueRepository.findByCharacAndProperty(charac, property);
//        }
//
//        if (characPropertyValue == null){
//            characPropertyValue = new CharacPropertyValue(charac, property, value);
//        }
//
//        if (!characPropertyValue.getValue().equals(value)){
//            characPropertyValue.setValue(value);
//        }

        return characPropertyValue;
    }
}
