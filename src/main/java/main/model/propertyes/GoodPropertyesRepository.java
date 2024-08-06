package main.model.propertyes;

import main.model.goods.Product;
import org.springframework.data.repository.CrudRepository;

public interface GoodPropertyesRepository extends CrudRepository<GoodPropertyes, Integer> {
    void deleteALLByProductAndName(Product product, String name);
}
