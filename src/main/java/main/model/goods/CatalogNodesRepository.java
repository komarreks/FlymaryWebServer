package main.model.goods;

import org.springframework.data.repository.CrudRepository;

public interface CatalogNodesRepository extends CrudRepository<CatalogNodes, Integer> {

    CatalogNodes findById(int id);
}
