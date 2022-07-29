package br.com.vemser.petshop.dto.cliente;

import br.com.vemser.petshop.enums.TipoPet;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClienteDadosRelatorioDTO {
    @Schema(description = "Identificador único do cliente")
    private Integer idCliente;
    @Schema(description = "Nome do cliente")
    private String nome;
    @Schema(description = "Email do cliente")
    private String email;
    @Schema(description = "Nome do pet do cliente")
    private String nomePet;
    @Schema(description = "Tipo do pet")
    private TipoPet tipoPet;
    @Schema(description = "Número do telefone/celular do cliente")
    private String telefone;
    @Schema(description = "Descricção do contato")
    private String descricao;


}
