package br.com.vemser.petshop.entity;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;

@Document(collection = "pedido_mensal")
@RequiredArgsConstructor
@Getter
@Setter
public class PedidoMensalEntity {

    @Id
    @Field("_id")
    private Integer idPedidoMensal;

    @Field(name = "mes")
    private Integer mes;

    @Field(name = "ano")
    private Integer ano;

    @Field(name = "total_de_pedidos")
    private Integer totalPedido;


}
