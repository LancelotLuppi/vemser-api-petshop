package br.com.vemser.petshop.repository;

import br.com.vemser.petshop.entity.PedidoMensalEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PedidosMensalRepository extends MongoRepository<PedidoMensalEntity, Integer> {

    Optional<PedidoMensalEntity> findPedidosByMesAndAno(Integer mes, Integer ano);
}
