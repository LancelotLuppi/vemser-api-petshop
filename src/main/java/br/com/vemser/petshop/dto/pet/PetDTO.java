package br.com.vemser.petshop.dto.pet;

import br.com.vemser.petshop.dto.pet.PetCreateDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;


@Data
public class PetDTO extends PetCreateDTO {

    @Schema(description = "Identificador único do pet")
    private Integer idPet;

    @Schema(description = "Identificador único do cliente")
    private Integer idCliente;
}
