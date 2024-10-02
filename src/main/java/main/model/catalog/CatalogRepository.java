package main.model.catalog;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CatalogRepository extends JpaRepository<Catalog, String> {

    Catalog findById1c(String id1c);

    List<Catalog> findByDeleted(int deleted);
}
