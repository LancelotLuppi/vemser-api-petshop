package br.com.vemser.petshop.entity;

import br.com.vemser.petshop.enums.TipoPet;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Pet {
    private Integer idPet;
    private Integer idCliente;
    private String nome;
    private TipoPet tipoPet;
    private String raca;
    private Integer pelagem;
    private Integer porte;
    private Integer idade;
}
