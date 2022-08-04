package br.com.vemser.petshop.dto.login;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class LoginDTO {
    private Integer idUsuario;
    private String username;
    private List<String> cargos;
}
