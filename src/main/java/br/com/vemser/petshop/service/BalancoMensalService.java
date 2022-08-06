package br.com.vemser.petshop.service;

import br.com.vemser.petshop.entity.BalancoMensalEntity;
import br.com.vemser.petshop.entity.PedidoEntity;
import br.com.vemser.petshop.exception.EntidadeNaoEncontradaException;
import br.com.vemser.petshop.repository.BalancoMensalRepository;
import br.com.vemser.petshop.repository.SequencesMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BalancoMensalService {
    private final BalancoMensalRepository balancoMensalRepository;
    private final SequencesMongoService sequencesMongoService;
    private final MongoTemplate mongoTemplate;


    public BalancoMensalEntity getBalancoMesAtual() throws EntidadeNaoEncontradaException {
        LocalDate localDate = LocalDate.now();
        return getBalancoByMesAndAno(localDate.getMonthValue(), localDate.getYear());
    }

    public BalancoMensalEntity getBalancoByMesAndAno(Integer mes, Integer ano) throws EntidadeNaoEncontradaException {
        return balancoMensalRepository.findBalancoMensalEntitiesByMesAndAno(mes, ano)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Não existem dados disponíveis para a data informada"));
    }

    public void atualizarBalanco(PedidoEntity pedidoEntity) {
        Optional<BalancoMensalEntity> balancoMensalEntity = balancoMensalRepository.findBalancoMensalEntitiesByMesAndAno(
                pedidoEntity.getDataEHora().getMonthValue(), pedidoEntity.getDataEHora().getYear());

        if (balancoMensalEntity.isPresent()) {
            BalancoMensalEntity balancoMensalEntityUpdate = balancoMensalEntity.get();
            System.out.println(balancoMensalEntityUpdate.getIdBalancoMensal());
            balancoMensalEntityUpdate.setLucroBruto(balancoMensalEntityUpdate.getLucroBruto() + pedidoEntity.getValor());
            balancoMensalEntityUpdate.setTotalDePedidos(balancoMensalEntityUpdate.getTotalDePedidos() + 1);
            mongoTemplate.save(balancoMensalEntityUpdate, "balanco_mensal");
        } else {
            BalancoMensalEntity balancoMensalEntityNew = new BalancoMensalEntity();
            balancoMensalEntityNew.setAno(pedidoEntity.getDataEHora().getYear());
            balancoMensalEntityNew.setMes(pedidoEntity.getDataEHora().getMonthValue());
            balancoMensalEntityNew.setTotalDePedidos(1);
            balancoMensalEntityNew.setLucroBruto(pedidoEntity.getValor());
            balancoMensalEntityNew.setIdBalancoMensal(sequencesMongoService.getIdByEntidade("balanco_mensal"));
            balancoMensalRepository.save(balancoMensalEntityNew);
        }
    }
}
