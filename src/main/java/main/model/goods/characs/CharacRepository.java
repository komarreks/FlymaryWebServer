package main.model.goods.characs;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CharacRepository extends JpaRepository<Charac, Long> {

    Charac findById1c(String id1c);

    Charac findById(long id);
}