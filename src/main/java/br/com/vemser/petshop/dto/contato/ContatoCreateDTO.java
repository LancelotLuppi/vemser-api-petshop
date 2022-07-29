package br.com.vemser.petshop.dto.contato;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class ContatoCreateDTO {

    @Schema(description = "Telefone do cliente cadastrado no sistema, apenas números", example = "(54)99999-0812")
    @NotNull
    private String telefone;

    @Schema(description = "Informações adicionais sobre o telefone informado", example = "celular pessoal")
    @NotNull
    @NotBlank
    @Size(max = 100)
    private String descricao;
}
