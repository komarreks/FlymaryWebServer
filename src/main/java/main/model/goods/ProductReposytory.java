package main.model.goods;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProductReposytory extends CrudRepository<Product, UUID> {

    Product findById1c(String id1c);

    Optional<Product> findById(UUID id);
}
