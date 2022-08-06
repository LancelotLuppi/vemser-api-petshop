package br.com.vemser.petshop.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;

@Document(collection = "balanco_mensal")
@RequiredArgsConstructor
@Getter
@Setter
public class BalancoMensalEntity {

    @Id
    @Field("_id")
    private Integer idBalancoMensal;

    @Field(name = "mes")
    private Integer mes;

    @Field(name = "ano")
    private Integer ano;

    @Field(name = "lucro_bruto")
    private Double lucroBruto;
}
