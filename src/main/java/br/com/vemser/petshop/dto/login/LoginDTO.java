package br.com.vemser.petshop.dto.login;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginDTO {
    private Integer idUsuario;
    private String username;
}
