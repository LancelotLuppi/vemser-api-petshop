package br.com.vemser.petshop.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ClienteDTO extends ClienteCreateDTO {
    @Schema(description = "Identificador único do cliente")
    private Integer idCliente;

    @Schema(description = "Quantidade de pedidos relacionados com o id do cliente")
    @NotNull
    private Integer quantidadeDePedidos;

    @Schema(description = "Valor somado de todos os pedidos em aberto ou em andamento")
    @NotNull
    private Integer valorPagamento;
}
