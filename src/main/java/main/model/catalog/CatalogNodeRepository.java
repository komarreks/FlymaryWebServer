package main.model.catalog;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CatalogNodeRepository extends JpaRepository<CatalogNode, String> {

    CatalogNode findById1c(String id1c);

    List<CatalogNode> findByCatalogAndDeletedAndParentOrderBySorting(Catalog catalog, int deleted, CatalogNode parent);

    List<CatalogNode> findByParentAndDeletedOrderBySorting(CatalogNode catalogNode, int deleted);
}
