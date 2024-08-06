package main.model.goods;

import org.springframework.data.repository.CrudRepository;

public interface ProductReposytory extends CrudRepository<Product, Integer> {

    Product findById1c(String id1c);
}
