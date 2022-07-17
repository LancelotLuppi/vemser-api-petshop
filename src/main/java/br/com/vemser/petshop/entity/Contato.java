package br.com.vemser.petshop.entity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Contato {
    private Integer idContato;
    private Integer idCliente;
    private Integer telefone;
    private String descricao;
    private String email;
}
