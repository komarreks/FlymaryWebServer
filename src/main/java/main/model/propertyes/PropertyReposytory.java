package main.model.propertyes;

import org.springframework.data.repository.CrudRepository;

public interface PropertyReposytory extends CrudRepository<Property, Integer> {

    Property findById1c(String id1c);

}
