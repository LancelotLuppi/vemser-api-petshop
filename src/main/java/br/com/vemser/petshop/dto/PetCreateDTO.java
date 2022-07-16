package br.com.vemser.petshop.dto;

import br.com.vemser.petshop.entity.Cliente;
import br.com.vemser.petshop.enums.TipoPet;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class PetCreateDTO {

    @NotBlank
    private Cliente cliente;

    @NotNull
    @NotBlank
    private String nome;

    @NotNull
    private TipoPet tipoPet;

    @NotBlank
    @NotNull
    private String raca;

    @NotNull
    @NotEmpty
    private Integer pelagem;

    @NotNull
    @NotEmpty
    private Integer porte;

    @NotNull
    @NotEmpty
    private Integer idade;
}
