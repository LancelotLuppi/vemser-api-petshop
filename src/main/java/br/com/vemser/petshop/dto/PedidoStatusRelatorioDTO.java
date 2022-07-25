package br.com.vemser.petshop.dto;

import br.com.vemser.petshop.enums.StatusPedido;
import br.com.vemser.petshop.enums.TipoPet;
import br.com.vemser.petshop.enums.TipoServico;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoStatusRelatorioDTO {
    @Schema(description = "Identificador único do pedido")
    private Integer idCliente;
    @Schema(description = "nome do cliente")
    private String nomeCliente;
    @Schema(description = "email do cliente")
    private String email;
    @Schema(description = "nome do pet do cliente")
    private String nomePet;
    @Schema(description = "tipo do pet do cliente (CACHORRO/GATO)")
    private TipoPet tipoPet;
    @Schema(description = "status atual do pedido")
    private StatusPedido status;
    @Schema(description = "tipo do serviço contratado")
    private TipoServico servico;
    @Schema(description = "valor do serviço")
    private Double valor;
}
