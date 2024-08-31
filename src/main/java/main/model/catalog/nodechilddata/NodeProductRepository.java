package main.model.catalog.nodechilddata;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface NodeProductRepository extends CrudRepository<NodeProduct, UUID> {
}