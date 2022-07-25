package br.com.vemser.petshop.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @SequenceGenerator(name = "CLIENTE_SEQ", sequenceName = "seq_id_cliente", allocationSize = 1)
    @Column(name = "id_cliente")
    private Integer idCliente;

    @Column(name = "nome")
    private String nome;

    @Column(name = "quantidade_pedidos")
    private Integer quantidadeDePedidos;

    @Column(name = "valor_pagamento")
    private Double valorPagamento;

    @Column(name = "email")
    private String email;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY,
        cascade = CascadeType.ALL, orphanRemoval = true,
        mappedBy = "cliente")
    private Set<ContatoEntity> contatos;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true,
            mappedBy = "cliente")
    private Set<PetEntity> pets;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,orphanRemoval = true,
            mappedBy = "cliente")
    private Set<PedidoEntity> pedidos;
}
