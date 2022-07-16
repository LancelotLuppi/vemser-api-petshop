package br.com.vemser.petshop.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ClienteCreateDTO {

    @NotBlank
    @NotNull
    private String nome;

    @NotNull
    private Integer quantidadeDePedidos;
}
