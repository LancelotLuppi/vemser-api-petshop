package br.com.vemser.petshop.repository;

import br.com.vemser.petshop.entity.ContatoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ContatoRepository extends JpaRepository<ContatoEntity, Integer> {

    @Query("delete ctt " +
            " from contato ctt " +
            " join ctt.cliente c " +
            " where (c.idCliente = :idCliente)")
    void deleteByClienteId(@Param("idCliente") Integer idCliente);
}


