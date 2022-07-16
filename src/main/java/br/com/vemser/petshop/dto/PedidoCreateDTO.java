package br.com.vemser.petshop.dto;

import br.com.vemser.petshop.entity.Cliente;
import br.com.vemser.petshop.entity.Pet;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class PedidoCreateDTO {

    @NotNull
    private Integer idCliente;

    @NotNull
    private Integer idPet;

    @NotNull
    @NotBlank
    private Double valor;

    @NotNull
    @NotBlank
    private String descricao;
}
