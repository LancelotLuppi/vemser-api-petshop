package br.com.vemser.petshop.dto;

import br.com.vemser.petshop.entity.Cliente;
import br.com.vemser.petshop.entity.Pet;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class PedidoCreateDTO {

    @Schema(description = "Valor do pedido gerado a ser pagado", example = "75")
    @NotNull
    private Integer valor;

    @Schema(description = "Informações adicionais sobre o pedido", example = "Banho e tosa")
    @NotNull
    @NotBlank
    private String descricao;
}
