package br.com.vemser.petshop.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.Data;

@Data
public class PedidoDTO extends PedidoCreateDTO{

    @Schema(description = "Identificador único do pedido")
    private Integer idPedido;

    @Schema(description = "Identificador único do cliente relacionado com o pedido")
    private Integer idCliente;

    @Schema(description = "Identificador único do pet relacionado com o pedido")
    private Integer idPet;
}
