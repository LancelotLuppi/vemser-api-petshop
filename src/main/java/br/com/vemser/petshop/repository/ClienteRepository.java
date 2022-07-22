package br.com.vemser.petshop.repository;

import br.com.vemser.petshop.dto.ClienteDadosRelatorioDTO;
import br.com.vemser.petshop.entity.ClienteEntity;
import ch.qos.logback.core.net.server.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ClienteRepository extends JpaRepository<ClienteEntity, Integer> {

    @Query(value = "select new br.com.vemser.petshop.dto.ClienteDadosRelatorioDTO(" +
            " c.idCliente," +
            " c.nome," +
            " c.email," +
            " pety.nome," +
            " pety.tipoPet," +
            " ctt.telefone," +
            " ctt.descricao" +
            ")" +
            " FROM cliente c" +
            " join c.pets pety" +
            " join c.contatos ctt" +
            " where (c.idCliente = :idCliente)")
    List<ClienteDadosRelatorioDTO> relatorioCliente(@Param("idCliente") Integer idCliente);
}
