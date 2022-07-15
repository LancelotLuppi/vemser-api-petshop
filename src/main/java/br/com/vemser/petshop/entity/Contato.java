package br.com.vemser.petshop.entity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Contato {
    private Integer idContato;
    private Cliente cliente;
    private Integer telefone;
    private String descricao;
}
