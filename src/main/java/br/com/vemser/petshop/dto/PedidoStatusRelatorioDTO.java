package br.com.vemser.petshop.dto;

import br.com.vemser.petshop.enums.StatusPedido;
import br.com.vemser.petshop.enums.TipoPet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoStatusRelatorioDTO {
    private Integer idCliente;
    private String nomeCliente;
    private String email;
    private String nomePet;
    private String tipoPet;
    private String status;
    private Double valor;
}
