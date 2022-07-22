package br.com.vemser.petshop.service;

import br.com.vemser.petshop.entity.PedidoEntity;
import br.com.vemser.petshop.entity.PetEntity;
import br.com.vemser.petshop.enums.TipoPet;
import br.com.vemser.petshop.enums.TipoServico;
import org.springframework.stereotype.Service;

@Service
public class CalculadoraService {

    public Double calcularValorDoPedido(PedidoEntity pedido, PetEntity pet) {
        if(pet.getTipoPet().equals(TipoPet.CACHORRO)) {
            return calculoCachorro(pedido, pet);
        }
        return calculoGato(pedido, pet);
    }

    public Double calculoCachorro(PedidoEntity pedido, PetEntity pet) {
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
        } else if (pedido.getServico().equals(TipoServico.ADESTRAMENTO)) {
            return 250.00;
        }
        return 0.00;
    }

    public Double calculoGato(PedidoEntity pedido, PetEntity pet) {
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
        } else if (pedido.getServico().equals(TipoServico.ADESTRAMENTO)) {
            return 120.00;
        }
        return 0.00;
    }
}
