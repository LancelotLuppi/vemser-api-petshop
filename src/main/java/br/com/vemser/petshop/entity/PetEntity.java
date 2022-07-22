package br.com.vemser.petshop.entity;

import br.com.vemser.petshop.enums.PelagemPet;
import br.com.vemser.petshop.enums.PortePet;
import br.com.vemser.petshop.enums.TipoPet;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

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

    @Column(name = "tipo")
    private TipoPet tipoPet;

    @Column(name = "raca")
    private String raca;

    @Column(name = "pelagem")
    private PelagemPet pelagem;

    @Column(name = "porte")
    private PortePet porte;

    @Column(name = "idade")
    private Integer idade;


    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", referencedColumnName = "id_cliente")
    private ClienteEntity cliente;
}
