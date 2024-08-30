package main.model.catalog;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface CatalogRepository extends CrudRepository<Catalog, UUID> {

    Catalog findById1c(String id1c);
}
