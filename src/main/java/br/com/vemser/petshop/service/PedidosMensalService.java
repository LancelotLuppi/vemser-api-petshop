package br.com.vemser.petshop.service;

import br.com.vemser.petshop.entity.BalancoMensalEntity;
import br.com.vemser.petshop.entity.PedidoEntity;
import br.com.vemser.petshop.entity.PedidoMensalEntity;
import br.com.vemser.petshop.exception.EntidadeNaoEncontradaException;
import br.com.vemser.petshop.repository.PedidosMensalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PedidosMensalService {

    private final PedidosMensalRepository pedidosMensalRepository;

    private final SequencesMongoService sequencesMongoService;
    private final MongoTemplate mongoTemplate;


    public PedidoMensalEntity getPedidoMesAtual() throws EntidadeNaoEncontradaException {
        LocalDate localDate = LocalDate.now();
        return getPedidoByMesAndAno(localDate.getMonthValue(), localDate.getYear());
    }

    public PedidoMensalEntity getPedidoByMesAndAno(Integer mes, Integer ano) throws EntidadeNaoEncontradaException {
        return pedidosMensalRepository.findPedidosByMesAndAno(mes, ano)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Não existem dados disponíveis para a data informada"));
    }

    public void atualizarPedidos(PedidoEntity pedidoEntity) {
        Optional<PedidoMensalEntity> pedidoMensalEntity = pedidosMensalRepository.findPedidosByMesAndAno(
                pedidoEntity.getDataEHora().getMonthValue(), pedidoEntity.getDataEHora().getYear());

        if (pedidoMensalEntity.isPresent()) {
            PedidoMensalEntity pedidoMensalEntityUpdate = pedidoMensalEntity.get();
            System.out.println(pedidoMensalEntityUpdate.getIdPedidoMensal());
            pedidoMensalEntityUpdate.setTotalPedido(pedidoMensalEntityUpdate.getTotalPedido() + 1);
            mongoTemplate.save(pedidoMensalEntityUpdate, "pedido_mensal");
        } else {
            PedidoMensalEntity pedidoMensalEntityNovo = new PedidoMensalEntity();
            pedidoMensalEntityNovo.setAno(pedidoEntity.getDataEHora().getYear());
            pedidoMensalEntityNovo.setMes(pedidoEntity.getDataEHora().getMonthValue());
            pedidoMensalEntityNovo.setTotalPedido(1);
            pedidoMensalEntityNovo.setIdPedidoMensal(sequencesMongoService.getIdByEntidade("pedido_mensal"));
            pedidosMensalRepository.save(pedidoMensalEntityNovo);
        }
    }
}
