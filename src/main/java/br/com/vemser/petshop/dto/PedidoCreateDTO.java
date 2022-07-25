package br.com.vemser.petshop.dto;

import br.com.vemser.petshop.enums.TipoServico;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class PedidoCreateDTO {

    @Schema(description = "Indica o tipo de serviço que será realizado", example = "BANHO")
    @NotNull
    private TipoServico servico;

    @Schema(description = "Informações adicionais sobre o pedido", example = "Pata esquerda traseira machucada")
    @NotNull
    @NotBlank
    private String descricao;
}
