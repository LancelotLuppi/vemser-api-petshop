package br.com.vemser.petshop.enums;

import java.util.Arrays;

public enum PelagemPet {
    CURTO(0),
    MEDIO(1),
    LONGO(2);

    private Integer tipo;

    PelagemPet(Integer tipo) {
        this.tipo = tipo;
    }

    public Integer getTipo() {
        return tipo;
    }
    public static PelagemPet ofTipo(Integer tipo){
        return Arrays.stream(PelagemPet.values())
                .filter(tp -> tp.getTipo().equals(tipo))
                .findFirst()
                .get();
    }
}
