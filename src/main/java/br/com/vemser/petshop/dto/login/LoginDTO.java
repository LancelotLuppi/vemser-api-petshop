package br.com.vemser.petshop.dto.login;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class LoginDTO {
    private Integer idUsuario;
    private String username;
    private List<String> cargos;
}
