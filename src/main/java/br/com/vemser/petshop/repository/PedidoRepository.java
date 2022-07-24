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
            " where (:status is null OR ped.status = :status)")
    Page<PedidoStatusRelatorioDTO> listStatusPedido(@Param("status")StatusPedido status, PageRequest pageRequest);

    @Query(value = "select p " +
                    " from pedido p " +
                    " join p.cliente c " +
                   " where (c.idCliente = :idCliente) ")
    Page<PedidoEntity> listarPedidosPorClientePaginado(@Param("idCliente") Integer idCliente, PageRequest pageRequest);

    @Query(value = "select p " +
            " from pedido p " +
            " join p.pet pety " +
            " where (pety.idPet = :idPet) ")
    Page<PedidoEntity> listarPedidosPorPetPaginado(@Param("idPet") Integer idPet, PageRequest pageRequest);

    @Query(value = "select p " +
            " from pedido p " +
            " join p.cliente c " +
            " join p.pet pety " +
            " where (pety.idPet = :idPet AND c.idCliente = :idCliente) ")
    Page<PedidoEntity> listarPedidosPorClienteAndPetPaginado(@Param("idCliente") Integer idCliente,
                                                             @Param("idPet") Integer idPet,
                                                             PageRequest pageRequest);

}
