package br.com.vemser.petshop.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "cliente")
public class ClienteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CLIENTE_SEQ")
    @SequenceGenerator(name = "CLIENTE_SEQ", sequenceName = "seq_id_cliente")
    @Column(name = "id_cliente")
    private Integer idCliente;

    @Column(name = "nome")
    private String nome;

    @Column(name = "quantidade_pedidos")
    private Integer quantidadeDePedidos;

    @Column(name = "valor_pagamento")
    private Integer valorPagamento;

    @Column(name = "email")
    private String email;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY,
        mappedBy = "cliente",
        cascade = CascadeType.ALL,
        orphanRemoval = true)
    private Set<ContatoEntity> contatos;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "cliente",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Set<PetEntity> pets;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "cliente",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Set<PedidoEntity> pedidos;
}
