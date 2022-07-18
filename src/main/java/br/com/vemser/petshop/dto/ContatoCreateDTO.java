package br.com.vemser.petshop.dto;

import br.com.vemser.petshop.entity.Cliente;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class ContatoCreateDTO {

    @Schema(description = "Telefone do cliente cadastrado no sistema, apenas números", example = "999990812")
    @NotNull
    private Integer telefone;

    @Schema(description = "Informações adicionais sobre o telefone informado", example = "celular pessoal")
    @NotNull
    @NotBlank
    private String descricao;
}
