package br.com.vemser.petshop.enums;

import java.util.Arrays;

public enum PortePet {
    PEQUENO(0),
    MEDIO(1),
    GRANDE(2);

    private Integer tipo;

    PortePet(Integer tipo) {
        this.tipo = tipo;
    }

    public Integer getTipo() {
        return tipo;
    }
    public static PortePet ofTipo(Integer tipo){
        return Arrays.stream(PortePet.values())
                .filter(tp -> tp.getTipo().equals(tipo))
                .findFirst()
                .get();
    }
}
