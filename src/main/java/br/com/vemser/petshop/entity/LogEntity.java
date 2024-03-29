package br.com.vemser.petshop.entity;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import java.util.Date;

@Document(collection = "logs")
@Getter
@Setter
public class LogEntity {

    @Id
    @Field(name = "_id")
    private ObjectId id;
    @Field(name = "date")
    private Date date;
    @Field(name = "level")
    private String level;
    @Field(name = "message")
    private String message;
}
