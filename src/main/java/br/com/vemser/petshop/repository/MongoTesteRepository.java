package br.com.vemser.petshop.repository;


import br.com.vemser.petshop.entity.EntityTestMongo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoTesteRepository extends MongoRepository<EntityTestMongo, Integer> {
}
