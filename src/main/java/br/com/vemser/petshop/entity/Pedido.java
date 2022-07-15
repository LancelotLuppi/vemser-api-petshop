package br.com.vemser.petshop.entity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Pedido {
    private Integer idPedido;
    private Cliente cliente;
    private Pet pet;
    private Double valor;
    private String descricao;
    private Integer IdPet;
}
