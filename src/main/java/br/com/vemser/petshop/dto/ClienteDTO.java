package br.com.vemser.petshop.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ClienteDTO extends ClienteCreateDTO {

    @Schema(description = "Identificador único do cliente")
    private Integer idCliente;

    @Schema(description = "Quantidade de pedidos relacionados com o id do cliente", example = "0")
    @NotNull
    private Integer quantidadeDePedidos;
    // (DD)99999-4444
}
