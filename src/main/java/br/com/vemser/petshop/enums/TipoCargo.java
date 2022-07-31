package br.com.vemser.petshop.enums;

import java.util.Arrays;

public enum TipoCargo {

    ADMIN(1),
    USUARIO(2),
    TOSADOR(3),
    ATENDENTE(4);

    private Integer tipo;

    TipoCargo(Integer tipo) {
        this.tipo = tipo;
    }

    public Integer getTipo() {
        return tipo;
    }
    public static TipoCargo ofTipo(Integer tipo){
        return Arrays.stream(TipoCargo.values())
                .filter(tp -> tp.getTipo().equals(tipo))
                .findFirst()
                .get();
    }
}
