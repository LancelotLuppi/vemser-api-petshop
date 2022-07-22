package br.com.vemser.petshop.repository;

import br.com.vemser.petshop.dto.PedidoStatusRelatorioDTO;
import br.com.vemser.petshop.entity.PedidoEntity;
import br.com.vemser.petshop.enums.StatusPedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<PedidoEntity, Integer> {

    @Query(value = "select new br.com.vemser.petshop.dto.PedidoStatusRelatorioDTO(" +
            " c.idCliente, " +
            " c.nome, " +
            " c.email, " +
            " p.nome, " +
            " p.tipoPet, " +
            " ped.status, " +
            " ped.servico, " +
            " ped.valor " +
            ") " +
            " from pedido ped " +
            " join ped.cliente c " +
            " join ped.pet p " +
            " where (ped.status = :status)")
    Page<PedidoStatusRelatorioDTO> listStatusPedido(@Param("status")StatusPedido status, PageRequest pageRequest);
}
