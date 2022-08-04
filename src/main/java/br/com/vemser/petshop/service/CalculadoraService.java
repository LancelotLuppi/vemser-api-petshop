package br.com.vemser.petshop.service;

import br.com.vemser.petshop.entity.PedidoEntity;
import br.com.vemser.petshop.entity.PetEntity;
import br.com.vemser.petshop.enums.TipoPet;
import br.com.vemser.petshop.enums.TipoServico;
import org.springframework.stereotype.Service;

@Service
public class CalculadoraService {
    private static final String MESSAGE_ERROR = "Desculpe, mas algo deu errado :(";

    public Double calcularValorDoPedido(PedidoEntity pedido, PetEntity pet) {
        if(pet.getTipoPet().equals(TipoPet.CACHORRO)) {
            return calcularParaCachorro(pedido, pet);
        }
        return calcularParaGato(pedido, pet);
    }

    public Double calcularParaCachorro(PedidoEntity pedido, PetEntity pet) {
        if(pedido.getServico().equals(TipoServico.BANHO)) {
            switch (pet.getPorte()) {
                case PEQUENO -> {
                    return 50.00;
                }
                case MEDIO -> {
                    return 65.00;
                }
                case GRANDE -> {
                    return 90.00;
                }
            }
        } else if (pedido.getServico().equals(TipoServico.TOSA)) {
            switch (pet.getPelagem()) {
                case CURTO -> {
                    return 35.00;
                }
                case MEDIO -> {
                    return 55.00;
                }
                case LONGO -> {
                    return 70.00;
                }
            }
        } else if (pedido.getServico().equals(TipoServico.CORTE_DE_UNHA)) {
            switch (pet.getPorte()) {
                case PEQUENO -> {
                    return 15.00;
                }
                case MEDIO -> {
                    return 20.00;
                }
                case GRANDE -> {
                    return 45.00;
                }
            }
        }
        return 250.0;
    }

    public Double calcularParaGato(PedidoEntity pedido, PetEntity pet) {
        if(pedido.getServico().equals(TipoServico.BANHO)) {
            switch (pet.getPorte()) {
                case PEQUENO -> {
                    return 25.00;
                }
                case MEDIO -> {
                    return 45.00;
                }
                case GRANDE -> {
                    return 60.00;
                }
            }
        } else if (pedido.getServico().equals(TipoServico.TOSA)) {
            switch (pet.getPelagem()) {
                case CURTO -> {
                    return 30.00;
                }
                case MEDIO -> {
                    return 45.00;
                }
                case LONGO -> {
                    return 55.00;
                }
            }
        } else if (pedido.getServico().equals(TipoServico.CORTE_DE_UNHA)) {
            switch (pet.getPorte()) {
                case PEQUENO -> {
                    return 25.00;
                }
                case MEDIO -> {
                    return 40.00;
                }
                case GRANDE -> {
                    return 50.00;
                }
            }
        }
        return 120.0;
    }
}
