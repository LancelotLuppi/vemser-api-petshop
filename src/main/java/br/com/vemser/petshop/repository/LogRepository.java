package br.com.vemser.petshop.repository;

import br.com.vemser.petshop.entity.LogEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LogRepository extends MongoRepository<LogEntity, Integer> {
}
