package main.model.goods;

import org.springframework.data.repository.CrudRepository;

public interface ProductReposytory extends CrudRepository<Product, Long> {

    Product findById1c(String id1c);

    Product findById(long id);
}
