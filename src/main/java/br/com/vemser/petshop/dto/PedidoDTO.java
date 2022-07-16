package br.com.vemser.petshop.dto;

import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.Data;

@Data
public class PedidoDTO extends PedidoCreateDTO{

    private Integer idPedido;
}
