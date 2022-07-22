package br.com.vemser.petshop.enums;

import java.util.Arrays;

public enum TipoServico {
    BANHO(0),
    TOSA(1),
    CORTE_DE_UNHA(2),
    ADESTRAMENTO(3);

    private Integer tipo;

    TipoServico(Integer tipo) {
        this.tipo = tipo;
    }

    public Integer getTipo() {
        return tipo;
    }
    public static TipoServico ofTipo(Integer tipo){
        return Arrays.stream(TipoServico.values())
                .filter(tp -> tp.getTipo().equals(tipo))
                .findFirst()
                .get();
    }
}
