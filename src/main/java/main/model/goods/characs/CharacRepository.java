package main.model.goods.characs;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CharacRepository extends JpaRepository<Charac, String> {

    Charac findById1c(String id1c);

    Optional<Charac> findById(String id);
}