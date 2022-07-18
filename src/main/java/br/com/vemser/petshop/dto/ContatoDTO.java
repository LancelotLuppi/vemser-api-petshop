package br.com.vemser.petshop.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ContatoDTO extends ContatoCreateDTO {

    @Schema(description = "Identificador único do contato")
    private Integer idContato;

    @Schema(description = "Identificador único do cliente dono do contato")
    private Integer idCliente;
}
