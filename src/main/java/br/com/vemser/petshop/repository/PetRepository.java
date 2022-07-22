package br.com.vemser.petshop.repository;

import br.com.vemser.petshop.entity.PetEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PetRepository extends JpaRepository<PetEntity, Integer> {

    @Query(value = " select a " +
            " FROM animal a " +
            " join a.cliente c" +
            " where c.idCliente = :idCliente")
    Page<PetEntity> findById(@Param("idCliente") Integer idCliente, PageRequest pageRequest);


}
