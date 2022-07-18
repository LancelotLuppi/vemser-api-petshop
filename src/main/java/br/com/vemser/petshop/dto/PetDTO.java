package br.com.vemser.petshop.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
public class PetDTO extends PetCreateDTO{

    @Schema(description = "Identificador único do pet")
    private Integer idPet;

    @Schema(description = "Identificador único do cliente")
    private Integer idCliente;
}
