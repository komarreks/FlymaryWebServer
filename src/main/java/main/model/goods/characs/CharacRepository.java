package main.model.goods.characs;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CharacRepository extends JpaRepository<Charac, UUID> {

    Charac findById1c(String id1c);

    Optional<Charac> findById(UUID id);
}