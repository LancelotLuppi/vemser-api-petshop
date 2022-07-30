package br.com.vemser.petshop.dto.login;

import lombok.Data;

@Data
public class LoginStatusDTO {

    private Integer idUsuario;
    private String username;
    private boolean ativado;
}
