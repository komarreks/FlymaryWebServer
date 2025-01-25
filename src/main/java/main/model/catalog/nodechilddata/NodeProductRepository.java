package main.model.catalog.nodechilddata;

import main.model.catalog.CatalogNode;
import main.model.goods.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface NodeProductRepository extends JpaRepository<NodeProduct, String> {

    List<NodeProduct> findByNodeIn(List<CatalogNode> nodes);
}