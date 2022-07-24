package br.com.vemser.petshop.entity;

import br.com.vemser.petshop.enums.StatusPedido;
import br.com.vemser.petshop.enums.TipoServico;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.boot.model.relational.Database;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "pedido")
public class PedidoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PEDIDO_SEQ")
    @SequenceGenerator(name = "PEDIDO_SEQ", sequenceName = "seq_id_pedido", allocationSize = 1)
    @Column(name = "id_pedido")
    private Integer idPedido;

    @Enumerated(EnumType.STRING)
    @Column(name = "servico")
    private TipoServico servico;

    @Column(name = "valor")
    private Double valor;

    @Column(name = "descricao")
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusPedido status;

    @Column(name = "data_e_hora_gerada")
    private LocalDateTime dataEHora;


    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", referencedColumnName = "id_cliente")
    private ClienteEntity cliente;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_animal", referencedColumnName = "id_animal")
    private PetEntity pet;


}
