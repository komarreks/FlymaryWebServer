package main.model.propertyes;

import main.model.goods.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PropertyValueReaper {

    @Autowired
    GoodPropertyValueRepository goodPropertyValueRepository;
    @Autowired
    PropertyReposytory propertyReposytory;

    public GoodPropertyValue findPropertyValue(Product product, String name, String value){
        Property property = propertyReposytory.findByName(name);

        GoodPropertyValue goodPropertyValue = null;

        if (!(product.getId() == null)){
            goodPropertyValue = goodPropertyValueRepository.findByProductAndProperty(product, property);
        }

        if (goodPropertyValue == null){
            goodPropertyValue = new GoodPropertyValue(product, property, value);
        }

        if (!goodPropertyValue.getValue().equals(value)){
            goodPropertyValue.setValue(value);
        }

        return goodPropertyValue;
    }
}
