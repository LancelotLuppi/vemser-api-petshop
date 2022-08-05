package br.com.vemser.petshop.repository;

import br.com.vemser.petshop.entity.SequencesMongoEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SequencesMongoRepository extends MongoRepository<SequencesMongoEntity, Integer> {

    Optional<SequencesMongoEntity> findSequencesMongoEntitiesByEntidade(String entidade);
}
