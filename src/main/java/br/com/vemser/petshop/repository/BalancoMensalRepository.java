package br.com.vemser.petshop.repository;

import br.com.vemser.petshop.entity.BalancoMensalEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BalancoMensalRepository extends MongoRepository<BalancoMensalEntity, Integer> {

    public BalancoMensalEntity findBalancoMensalEntitiesByMesAndAno(Integer mes, Integer ano);
}
