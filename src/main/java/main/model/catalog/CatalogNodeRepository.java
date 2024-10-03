package main.model.catalog;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CatalogNodeRepository extends JpaRepository<CatalogNode, String> {

    CatalogNode findById1c(String id1c);
}
