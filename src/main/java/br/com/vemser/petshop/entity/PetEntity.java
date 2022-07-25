package br.com.vemser.petshop.entity;

import br.com.vemser.petshop.enums.PelagemPet;
import br.com.vemser.petshop.enums.PortePet;
import br.com.vemser.petshop.enums.TipoPet;
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
@Entity(name = "animal")
public class PetEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PET_SEQ")
    @SequenceGenerator(name = "PET_SEQ", sequenceName = "seq_id_animal", allocationSize = 1)
    @Column(name = "id_animal")
    private Integer idPet;

    @Column(name = "nome")
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private TipoPet tipoPet;

    @Column(name = "raca")
    private String raca;

    @Enumerated(EnumType.STRING)
    @Column(name = "pelagem")
    private PelagemPet pelagem;

    @Enumerated(EnumType.STRING)
    @Column(name = "porte")
    private PortePet porte;

    @Column(name = "idade")
    private Integer idade;


    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", referencedColumnName = "id_cliente")
    private ClienteEntity cliente;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,orphanRemoval = true,
            mappedBy = "pet")
    private Set<PedidoEntity> pedidos;
}
