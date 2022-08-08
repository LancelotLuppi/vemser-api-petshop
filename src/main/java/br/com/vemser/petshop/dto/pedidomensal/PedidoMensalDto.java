package br.com.vemser.petshop.dto.pedidomensal;

import lombok.Data;

@Data
public class PedidoMensalDto {

    private Integer mes;

    private Integer ano;

    private Integer totalPedido;
}
