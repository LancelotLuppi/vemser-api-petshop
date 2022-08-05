package br.com.vemser.petshop.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.*;

@Entity
@Document(collection = "animal")
@RequiredArgsConstructor
@Getter
@Setter
public class EntityTestMongo {

    @Id
    private Integer idPet;

    @Field(name = "nome")
    private String nome;

    @Field(name = "tipo")
    private String tipoPet;

    @Field(name = "raca")
    private String raca;

    @Field(name = "pelagem")
    private Integer pelagem;

    @Field(name = "porte")
    private Integer porte;

    @Field(name = "idade")
    private Integer idade;
}
