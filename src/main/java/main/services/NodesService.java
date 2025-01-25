package main.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.RequiredArgsConstructor;
import main.answers.LoadLine;
import main.answers.StatusLoad;
import main.dto.ChildNodeFilterDTO;
import main.dto.Mapper;
import main.dto.NodeDTO;
import main.dto.ProductDTO;
import main.model.catalog.Catalog;
import main.model.catalog.CatalogNode;
import main.model.catalog.CatalogNodeRepository;
import main.model.catalog.CatalogRepository;
import main.model.catalog.nodechilddata.NodeProduct;
import main.model.catalog.nodechilddata.NodeProductRepository;
import main.model.goods.Product;
import main.model.goods.ProductReposytory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NodesService {

    //region FIELDS
    private final CatalogNodeRepository repository;
    private final CatalogRepository catalogRepository;
    private final NodeProductRepository nodeProductRepository;
    private final ProductReposytory productReposytory;
    private final Mapper mapper;
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

    /**
     * Метод отправляет верхние категории каталога
     * @param catalogId
     * @return
     */
    public List<CatalogNode> getRoot(String catalogId){

        Catalog catalog = catalogRepository.findById(catalogId).orElse(null);

        if (catalog != null) {
            //return repository.findByCatalog(catalog);
            return repository.findByCatalogAndDeletedAndParentOrderBySorting(catalog, 0, null);
        }

        return null;
    }

    /**
     * Метод возвращает изображение категории товара в виде массива байтов
     * @param id
     * @return
     */
    public byte[] getImage(String id) {
        CatalogNode node = repository.findById(id).orElse(null);

        if (node == null) return null;

        return node.getImage64();
    }

    /**
     * Метод вовращает список продуктов товарной категории, включая вложенные
     * @param nodeId
     * @return
     */
    public List<ProductDTO> getProductsDTO(String nodeId) {

        List<CatalogNode> childNodes = repository.findByParentAndDeletedOrderBySorting(repository.findById(nodeId).orElse(null), 0);

        if (childNodes == null || childNodes.isEmpty()){
            CatalogNode catalogNode = repository.findById(nodeId).orElse(null);

            if (catalogNode != null) {
                if (childNodes == null){childNodes = new ArrayList<>();}
                childNodes.add(catalogNode);
            }
            else return null;
        }

        List<NodeProduct> nodeProducts = nodeProductRepository.findByNodeIn(childNodes);

        if (nodeProducts == null) return null;

        return mapper.transferToProductsDTO(nodeProducts);
    }

    public List<NodeDTO> getChildNodesDTO(String nodeId) {
        List<CatalogNode> childNodes = repository.findByParentAndDeletedOrderBySorting(repository.findById(nodeId).orElse(null), 0);

        if (childNodes == null || childNodes.isEmpty()) return new ArrayList<>();

        return mapper.transferToNodesDTO(childNodes);
    }
    //endregion
}
