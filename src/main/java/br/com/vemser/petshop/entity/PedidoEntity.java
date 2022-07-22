package br.com.vemser.petshop.entity;

import br.com.vemser.petshop.enums.StatusPedido;
import br.com.vemser.petshop.enums.TipoServico;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.boot.model.relational.Database;

import javax.persistence.*;
import java.time.LocalDate;

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

    @Column(name = "servico")
    private TipoServico servico;

    @Column(name = "valor")
    private Integer valor;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "status")
    private StatusPedido status;

    @Column(name = "data_e_hora_gerada")
    private LocalDate dataEHora;


    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", referencedColumnName = "id_cliente")
    private ClienteEntity cliente;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pet", referencedColumnName = "id_pet")
    private PetEntity pet;


}
