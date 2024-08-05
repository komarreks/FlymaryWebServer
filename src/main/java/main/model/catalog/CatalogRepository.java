package main.model.catalog;

import org.springframework.data.repository.CrudRepository;

public interface CatalogRepository extends CrudRepository<Catalog, Integer> {

    Catalog findById1c(String id1c);
}
