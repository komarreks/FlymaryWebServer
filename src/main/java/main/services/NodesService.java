package main.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.RequiredArgsConstructor;
import main.answers.LoadLine;
import main.answers.StatusLoad;
import main.model.catalog.CatalogNode;
import main.model.catalog.CatalogNodeRepository;
import main.model.catalog.CatalogRepository;
import main.model.catalog.nodechilddata.NodeProductRepository;
import main.model.goods.Product;
import main.model.goods.ProductReposytory;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class NodesService {

    //region FIELDS
    private final CatalogNodeRepository repository;
    private final CatalogRepository catalogRepository;
    private final NodeProductRepository nodeProductRepository;
    private final ProductReposytory productReposytory;
    //endregion

    //region METHODS
    /**
     * Метод распарсивает узлы, переданные в формате json и сохраняет в базу
     * @param jsNodes
     * @return
     */
    public StatusLoad createUpdate(ArrayNode jsNodes){

        StatusLoad statusLoad = new StatusLoad();

        for (JsonNode jsNode: jsNodes) {
            LoadLine loadLine = new LoadLine(jsNode.get("id1c").textValue());
            CatalogNode node = new CatalogNode();
            node.setId1c(jsNode.get("id1c").textValue());
            node.setName(jsNode.get("name").textValue());
            node.setSorting(jsNode.get("sorting").intValue());
            node.setDeleted(jsNode.get("deleted").intValue());
            node.setParent(repository.findById1c(jsNode.get("parent").textValue()));
            node.setCatalog(catalogRepository.findById1c(jsNode.get("catalog").textValue()));
            node.setVersion(jsNode.get("version").intValue());

            try {
                node.setImage64(jsNode.get("image64").binaryValue());
            } catch (IOException e) {
                node.setImage64(null);
            }

            ArrayNode productsId1c = (ArrayNode) jsNode.get("products");
            nodeProductRepository.deleteAll(node.getProducts());
            for (JsonNode productId1c: productsId1c) {
                String id1c = productId1c.textValue();
                Product product = productReposytory.findById1c(id1c);
                node.addProduct(product);
            }

            repository.save(node);

            loadLine.setStatus("Обновлен");
            statusLoad.addLog(loadLine);
        }

        return statusLoad;
    }
    //endregion
}
