package main.model.images;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ImageRepository extends CrudRepository<Image, UUID> {

    Image findById1c(String id1c);

}
