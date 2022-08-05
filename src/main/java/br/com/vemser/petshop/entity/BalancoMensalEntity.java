package br.com.vemser.petshop.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Document(collection = "balanco_mensal")
@RequiredArgsConstructor
@Getter
@Setter
public class BalancoMensalEntity {

    @Id
    @Field(name = "_id")
    private Integer idBalancoMensal;

    @Field(name = "mes")
    private Integer mes;

    @Field(name = "ano")
    private Integer ano;

    @Field(name = "total_de_pedidos")
    private Integer totalDePedidos;

    @Field(name = "lucro_bruto")
    private Double lucroBruno;
}
