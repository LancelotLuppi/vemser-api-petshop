package br.com.vemser.petshop.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ClienteCreateDTO {

    @Schema(description = "Nome do cliente no cadastro", example = "Gabriel Luppi")
    @NotBlank
    @NotNull
    private String nome;

    @Schema(description = "Email do cliente no cadastro, deve ser válido", example = "luppi.gabriel08@gmail.com")
    @NotBlank
    @NotNull
    @Email
    private String email;
}
