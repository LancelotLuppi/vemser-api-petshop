package br.com.vemser.petshop.repository;

import br.com.vemser.petshop.entity.PedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends JpaRepository<PedidoEntity, Integer> {
}
