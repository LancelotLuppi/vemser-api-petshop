package br.com.vemser.petshop.service;

import br.com.vemser.petshop.client.ConsumerPetshopClient;
import br.com.vemser.petshop.dto.balancomensal.BalancoMensalDTO;
import br.com.vemser.petshop.dto.login.LoginCreateDTO;
import br.com.vemser.petshop.dto.pedidomensal.PedidoMensalDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ConsumerPetshopService {
    @Value("${consumer.login}")
    private String login;

    @Value("${consumer.senha}")
    private String senha;
    private final ConsumerPetshopClient consumerPetshopClient;

    public BalancoMensalDTO getBalancoMesAtual() {
        return consumerPetshopClient.getMesAtual(getAuth());
    }

    public BalancoMensalDTO getBalancoByMesAndAno(Integer mes, Integer ano) {
        return consumerPetshopClient.getByMesAno(mes, ano, getAuth());
    }

    public PedidoMensalDto getPedidoMesAtual() {
        return consumerPetshopClient.getPedidosMesAtual(getAuth());
    }

    public PedidoMensalDto getPedidoByMesAndAno(Integer mes, Integer ano) {
        return consumerPetshopClient.getPedidosByMesAno(mes, ano, getAuth());
    }

    private Map<String, String> getAuth(){
        LoginCreateDTO loginCreateDTO = new LoginCreateDTO();
        loginCreateDTO.setUsername(login);
        loginCreateDTO.setSenha(senha);
        Map<String, String> header = new HashMap<>();
        header.put("Authorization", consumerPetshopClient.auth(loginCreateDTO));
        return header;
    }
}
