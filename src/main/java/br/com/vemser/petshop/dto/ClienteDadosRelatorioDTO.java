package br.com.vemser.petshop.dto;

import br.com.vemser.petshop.enums.TipoPet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClienteDadosRelatorioDTO {

    private Integer idCliente;
    private String nome;
    private String email;
    private String nomePet;
    private TipoPet tipoPet;
    private String telefone;
    private String descricao;


}
