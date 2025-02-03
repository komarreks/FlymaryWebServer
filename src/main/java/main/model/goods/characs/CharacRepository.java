package main.model.goods.characs;

import main.model.goods.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CharacRepository extends JpaRepository<Charac, String> {

    Charac findById1c(String id1c);

    Optional<Charac> findById(String id);

    List<Charac> findByProduct(Product product);
}