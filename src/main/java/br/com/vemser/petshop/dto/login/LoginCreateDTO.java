package br.com.vemser.petshop.dto.login;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LoginCreateDTO {

    @NotNull
    private String username;

    @NotNull
    private String senha;
}
