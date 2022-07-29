package br.com.vemser.petshop.dto.pedido;

import br.com.vemser.petshop.dto.pedido.PedidoCreateDTO;
import br.com.vemser.petshop.enums.StatusPedido;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class PedidoDTO extends PedidoCreateDTO {

    @Schema(description = "Identificador único do pedido")
    private Integer idPedido;

    @Schema(description = "Identificador único do cliente relacionado com o pedido")
    private Integer idCliente;

    @Schema(description = "Identificador único do pet relacionado com o pedido")
    private Integer idPet;

    @Schema(description = "Valor do pedido gerado a ser pagado")
    @NotNull
    private Integer valor;

    @Schema(description = "Indica o estado atual do pedido")
    private StatusPedido status;

    @Schema(description = "Data e hora em que o pedido foi gerado")
    private LocalDateTime data;
}
