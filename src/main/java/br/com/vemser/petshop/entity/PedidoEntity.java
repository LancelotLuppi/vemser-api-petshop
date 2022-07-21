package br.com.vemser.petshop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.boot.model.relational.Database;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "pedido")
public class PedidoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PEDIDO_SEQ")
    @SequenceGenerator(name = "PEDIDO_SEQ", sequenceName = "seq_id_pedido")
    @Column(name = "id_pedido")
    private Integer idPedido;

    @Column(name = "id_cliente")
    private Integer idCliente;

    @Column(name = "id_animal")
    private Integer idPet;

    @Column(name = "valor")
    private Integer valor;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "status")
    private String status;

    //private Double dataEHora;


    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", referencedColumnName = "id_cliente")
    private ClienteEntity clienteEntity;


}
