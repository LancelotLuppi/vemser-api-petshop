package br.com.vemser.petshop.entity;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ClienteEntity {
    private Integer idCliente;
    private String email;
    private String nome;
    private Integer quantidadeDePedidos;
}
