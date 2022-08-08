package br.com.vemser.petshop.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Document("sequences")
@RequiredArgsConstructor
@Getter
@Setter
public class SequencesMongoEntity {
    @Id
    @Field(name = "sequences")
    private ObjectId idSequence;

    @Field(name = "atual")
    private Integer atual;

    @Field(name = "entidade")
    private String entidade;
}
