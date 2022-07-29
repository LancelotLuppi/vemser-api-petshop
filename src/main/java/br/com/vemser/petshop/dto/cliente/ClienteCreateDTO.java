package br.com.vemser.petshop.dto.cliente;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class ClienteCreateDTO {

    @Schema(description = "Nome do cliente no cadastro", example = "Gabriel Luppi")
    @NotBlank
    @NotNull
    @Size(max = 100)
    private String nome;

    @Schema(description = "Email do cliente no cadastro, deve ser válido", example = "luppi.gabriel08@gmail.com")
    @NotBlank
    @NotNull
    @Email
    private String email;
}
