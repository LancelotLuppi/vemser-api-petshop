package br.com.vemser.petshop.dto;

import br.com.vemser.petshop.enums.TipoServico;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class PedidoCreateDTO {

    @Schema(description = "Indica o tipo de serviço que será realizado")
    @NotNull
    private TipoServico servico;

    @Schema(description = "Informações adicionais sobre o pedido", example = "Banho e tosa, alérgico a shampoo com químico forte")
    @NotNull
    @NotBlank
    private String descricao;
}
