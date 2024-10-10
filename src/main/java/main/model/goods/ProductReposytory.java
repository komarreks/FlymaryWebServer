package main.model.goods;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProductReposytory extends JpaRepository<Product, String> {

    Product findById1c(String id1c);

    Optional<Product> findById(String id);
}
