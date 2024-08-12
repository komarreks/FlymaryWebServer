package main.model.images;

import org.springframework.data.repository.CrudRepository;

public interface ImageRepository extends CrudRepository<Image, Long> {

    Image findById1c(String id1c);

}
