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
    private String telefone;
    private String descricao;
    private String email;
}
