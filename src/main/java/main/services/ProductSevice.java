package main.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.RequiredArgsConstructor;
import main.answers.LoadLine;
import main.answers.StatusLoad;
import main.dto.Mapper;
import main.dto.ProductDTO;
import main.model.goods.Product;
import main.model.goods.ProductReposytory;
import main.model.goods.characs.Charac;
import main.model.goods.characs.CharacRepository;
import main.model.goods.characs.characproperty.CharacPropertyPK;
import main.model.goods.characs.characproperty.CharacPropertyValue;
import main.model.goods.goodpropery.GoodPropertyPK;
import main.model.goods.goodpropery.GoodPropertyValue;
import main.model.images.Image;
import main.model.images.ImageRepository;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProductSevice {

    //region FIELDS
    private final ProductReposytory reposytory;
    private final CharacRepository characRepository;
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
            product.setPrice(jsProduct.get("price").decimalValue());
            product.setCount(jsProduct.get("count").decimalValue());

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

    /**
     * Метод создания/обновления характеристик товара вместе со свойствами из JSON массива
     * используется в REST API
     * @param jsCharacs
     * @return
     */
    public StatusLoad createUpdateCharacs(ArrayNode jsCharacs){
        StatusLoad statusLoad = new StatusLoad();

        jsCharacs.forEach(jsCharac -> {
            String id1c = jsCharac.get("id1c").textValue().trim();
            String productId1c = jsCharac.get("productId1c").textValue().trim();

            LoadLine loadLine = new LoadLine(id1c);

            Charac charac = characRepository.findById1c(id1c);

            if (charac == null){
                charac = new Charac();
                loadLine.setStatus("Загружен");
            }

            charac.setId1c(id1c);

            if (charac.getProduct() == null) charac.setProduct(reposytory.findById1c(productId1c));

            charac.setName(jsCharac.get("name").textValue());
            charac.setPrice(jsCharac.get("price").decimalValue());
            charac.setCount(jsCharac.get("count").decimalValue());

            charac.clearPropertyes();

            ArrayNode jsPropertyes = (ArrayNode) jsCharac.get("propertyes");

            for (JsonNode jsProp: jsPropertyes) {
                CharacPropertyValue characPropertyValue = new CharacPropertyValue();
                characPropertyValue.setCharacPropertyPK(new CharacPropertyPK(charac, jsProp.get("name").textValue()));

                switch (jsProp.get("type").textValue()){
                    case ("boolean"):
                        characPropertyValue.setBoolValue(jsProp.get("value").booleanValue());
                        break;
                    case ("digit"):
                        characPropertyValue.setDigitValue(new BigDecimal(jsProp.get("value").floatValue()));
                        break;
                    default:
                        characPropertyValue.setStringValue(jsProp.get("value").textValue());
                }

                charac.addProperty(characPropertyValue);
            }

            characRepository.save(charac);

            if (loadLine.getStatus() == null) loadLine.setStatus("Обновлен");
            statusLoad.addLog(loadLine);
        });

        return statusLoad;
    }

    /**
     * Метод возвращает из репозитория товар
     * @param productId1c
     * @return
     */
    public Product findById1c(String productId1c) {
        return reposytory.findById1c(productId1c);
    }

    /**
     * Метод возвращает из репозитория характеристику товара
     * @param id
     * @return
     */
    public Charac findCharacById1c(String id){
        return characRepository.findById1c(id);
    }

    public Product findById(String productId) {
        return reposytory.findById(productId).orElse(null);
    }

    public List<Charac> getCharacs(Product product){
        return characRepository.findByProduct(product).stream().filter(Charac::isVisible).toList();
    }

    /**
     * регулярное обновление цен
     * @param jsPices
     * @return
     */
    public StatusLoad regularUpdatePrice(ArrayNode jsPices) {
        StatusLoad statusLoad = new StatusLoad();

        for (JsonNode jsPrice: jsPices) {
            String productId = jsPrice.get("productId").textValue();
            String characId = jsPrice.get("characId").textValue();
            BigDecimal price = jsPrice.get("price").decimalValue();

            Product product = findById1c(productId);

            if (product == null) continue;

            Charac charac = findCharacById1c(characId);

            LoadLine loadLine = new LoadLine(product.getId1c());

            if (charac == null){
                product.setPrice(price);
                loadLine.setStatus("цена обновлена");
                reposytory.save(product);
            }else {
                charac.setPrice(price);
                loadLine.setStatus("цена "+ charac.getName() + " обновлена");
                characRepository.save(charac);
            }

            statusLoad.addLog(loadLine);
        }

        return statusLoad;
    }

    /**
     * Обновление остатков
     * @param jsCounts
     * @return
     */
    public StatusLoad updateCounts(ArrayNode jsCounts) {
        StatusLoad statusLoad = new StatusLoad();
        for (JsonNode jsCount: jsCounts) {
            String productId1c = jsCount.get("productId1c").textValue();
            String characId1c = jsCount.get("characId1c").textValue();
            BigDecimal count = jsCount.get("count").decimalValue();

            Product product = findById1c(productId1c);

            if (product == null) continue;

            Charac charac = findCharacById1c(characId1c);

            LoadLine loadLine = new LoadLine(product.getId1c());

            if (charac == null){
                product.setCount(count);
                loadLine.setStatus("Остаток обновлен");
                reposytory.save(product);
            }else {
                charac.setCount(count);
                loadLine.setStatus("остаток "+ charac.getName() + " обновлен");
                characRepository.save(charac);
            }

            statusLoad.addLog(loadLine);
        }
        return statusLoad;
    }

    public ProductDTO getProductDTOByIdProduct(String id) {
        Product product = findById(id);

        ProductDTO productDTO = new Mapper(this).transferToProductsDTO(product);

        return productDTO;
    }
    //endregion

}
