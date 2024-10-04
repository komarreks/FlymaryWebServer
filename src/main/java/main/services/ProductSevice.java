package main.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.RequiredArgsConstructor;
import main.answers.LoadLine;
import main.answers.StatusLoad;
import main.model.goods.Product;
import main.model.goods.ProductReposytory;
import main.model.propertyes.goodpropery.GoodPropertyPK;
import main.model.propertyes.goodpropery.GoodPropertyValue;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class ProductSevice {

    //region FIELDS
    private final ProductReposytory reposytory;
    //endregion

    //region METHODS
    /**
     * Метод создания/обновления товара вместе со свойствами из JSON массива
     * используется в REST API
     * @param jsProducts
     * @return
     */
    public StatusLoad createUpdate(ArrayNode jsProducts){
        StatusLoad statusLoad = new StatusLoad();

        for (JsonNode jsProduct: jsProducts) {
            Product product = reposytory.findById1c(jsProduct.get("id1c").textValue());

            LoadLine loadLine = new LoadLine(jsProduct.get("id1c").textValue());

            if (product == null){
                product = new Product();
                loadLine.setStatus("Создан");
            }

            product.setId1c(jsProduct.get("id1c").textValue());
            product.setName(jsProduct.get("name").textValue());

            product.clearPropertyes();
            ArrayNode jsPropertyes = (ArrayNode) jsProduct.get("propertyes");

            for (JsonNode jsProp: jsPropertyes) {
                GoodPropertyValue goodPropertyValue = new GoodPropertyValue();
                goodPropertyValue.setGoodPropertyPK(new GoodPropertyPK(product, jsProp.get("name").textValue()));

                switch (jsProp.get("type").textValue()){
                    case ("boolean"):
                        goodPropertyValue.setBoolValue(jsProp.get("value").booleanValue());
                        break;
                    case ("digit"):
                        goodPropertyValue.setDigitValue(new BigDecimal(jsProp.get("value").floatValue()));
                        break;
                    default:
                        goodPropertyValue.setStringValue(jsProp.get("value").textValue());
                }

                product.addProperty(goodPropertyValue);
            }

            reposytory.save(product);

            if (loadLine.getStatus() == null) loadLine.setStatus("Обновлен");
            statusLoad.addLog(loadLine);

        }

        return statusLoad;
    }
    //endregion

}
