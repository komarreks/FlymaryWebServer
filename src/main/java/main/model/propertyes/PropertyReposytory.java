package main.model.propertyes;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface PropertyReposytory extends CrudRepository<Property, UUID> {

    Property findById1c(String id1c);

    Property findByName(String name);

}
