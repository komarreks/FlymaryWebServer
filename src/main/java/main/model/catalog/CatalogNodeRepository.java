package main.model.catalog;

import org.springframework.data.repository.CrudRepository;

public interface CatalogNodeRepository extends CrudRepository<CatalogNode, Integer> {

    CatalogNode findById(int id);
}
