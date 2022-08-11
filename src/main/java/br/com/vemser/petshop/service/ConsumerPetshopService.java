package br.com.vemser.petshop.service;

import br.com.vemser.petshop.client.ConsumerPetshopClient;
import br.com.vemser.petshop.dto.balancomensal.BalancoMensalDTO;
import br.com.vemser.petshop.dto.pedidomensal.PedidoMensalDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConsumerPetshopService {
    private final ConsumerPetshopClient consumerPetshopClient;

    public BalancoMensalDTO getBalancoMesAtual() {
        return consumerPetshopClient.getMesAtual();
    }

    public BalancoMensalDTO getBalancoByMesAndAno(Integer mes, Integer ano) {
        return consumerPetshopClient.getByMesAno(mes, ano);
    }

    public PedidoMensalDto getPedidoMesAtual() {
        return consumerPetshopClient.getPedidosMesAtual();
    }

    public PedidoMensalDto getPedidoByMesAndAno(Integer mes, Integer ano) {
        return consumerPetshopClient.getPedidosByMesAno(mes, ano);
    }
}
