package br.com.vemser.petshop.dto.login;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class LoginCreateDTO {

    @NotNull
    @NotBlank
    private String username;

    @NotNull
    @NotBlank
    private String senha;
}
