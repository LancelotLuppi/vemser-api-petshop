package br.com.vemser.petshop.entity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PedidoEntity {
    private Integer idPedido;
    private Integer idCliente;
    private Integer idPet;
    private Integer valor;
    private String descricao;
}
