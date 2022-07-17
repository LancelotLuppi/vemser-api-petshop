package br.com.vemser.petshop.entity;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Cliente {
    private Integer idCliente;
    private String email;
    private String nome;
    private Integer quantidadeDePedidos;
}
