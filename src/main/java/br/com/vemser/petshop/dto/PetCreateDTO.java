package br.com.vemser.petshop.dto;

import br.com.vemser.petshop.entity.Cliente;
import br.com.vemser.petshop.enums.TipoPet;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.*;

@Data
public class PetCreateDTO {

    @Schema(description = "Nome do pet no cadastro", example = "Eros")
    @NotNull
    @NotBlank
    @Size(max = 50)
    private String nome;

    @Schema(description = "Informativo se o pet é um cachorro ou gato", example = "GATO")
    @NotNull
    private TipoPet tipoPet;

    @Schema(description = "Informativo da raça do pet", example = "SRD")
    @NotBlank
    @NotNull
    @Size(max = 50)
    private String raca;

    @Schema(description = "Informativo do tamanho da pelagem baseada na raça (0min - 9max)", example = "9")
    @NotNull
    @Min(0)
    @Max(9)
    private Integer pelagem;

    @Schema(description = "Informativo do porte do pet (0 muito pequeno - 9 muito grande)", example = "6")
    @NotNull
    @Min(0)
    @Max(9)
    private Integer porte;

    @Schema(description = "Idade do pet", example = "8")
    @NotNull
    @Min(0)
    @Max(50)
    private Integer idade;
}
