package br.com.vemser.petshop.dto;

import br.com.vemser.petshop.enums.StatusPedido;
import br.com.vemser.petshop.enums.TipoPet;
import br.com.vemser.petshop.enums.TipoServico;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoStatusRelatorioDTO {
    private Integer idCliente;
    private String nomeCliente;
    private String email;
    private String nomePet;
    private TipoPet tipoPet;
    private StatusPedido status;
    private TipoServico servico;
    private Double valor;
}
