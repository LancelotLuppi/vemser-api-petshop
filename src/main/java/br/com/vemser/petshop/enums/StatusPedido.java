package br.com.vemser.petshop.enums;

import java.util.Arrays;

public enum StatusPedido {
    CONCLUIDO(0),
    CANCELADO(1),
    EM_ANDAMENTO(2),
    ABERTO(3);

    private Integer tipo;

    StatusPedido(Integer tipo) {
        this.tipo = tipo;
    }

    public Integer getTipo() {
        return tipo;
    }
    public static StatusPedido ofTipo(Integer tipo){
        return Arrays.stream(StatusPedido.values())
                .filter(tp -> tp.getTipo().equals(tipo))
                .findFirst()
                .get();
    }
}
