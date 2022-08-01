package br.com.vemser.petshop.dto.login;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class LoginCreateDTO {

    @NotNull
    @NotBlank
    @Schema(description = "nome do usuario", example = "Kenobi")
    private String username;

    @NotNull
    @NotBlank
    @Schema(description = "senha do usuario", example = "kenobi")
    private String senha;
}
