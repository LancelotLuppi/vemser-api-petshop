package br.com.vemser.petshop.dto.login;

import br.com.vemser.petshop.enums.TipoCargo;
import lombok.Data;

@Data
public class LoginUpdateDTO {

    private String username;

    private TipoCargo tipoCargo;
}
