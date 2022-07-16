package br.com.vemser.petshop.dto;

import br.com.vemser.petshop.entity.Cliente;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class ContatoCreateDTO {

    @NotBlank
    private Integer idCliente;

    @NotNull
    @Size(min = 11, max = 11)
    private Integer telefone;

    @NotNull
    @NotBlank
    private String descricao;
}
