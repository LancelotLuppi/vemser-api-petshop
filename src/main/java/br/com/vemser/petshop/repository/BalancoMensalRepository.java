package br.com.vemser.petshop.repository;

import br.com.vemser.petshop.entity.BalancoMensalEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BalancoMensalRepository extends MongoRepository<BalancoMensalEntity, Integer> {

    Optional<BalancoMensalEntity> findBalancoMensalEntitiesByMesAndAno(Integer mes, Integer ano);
}
