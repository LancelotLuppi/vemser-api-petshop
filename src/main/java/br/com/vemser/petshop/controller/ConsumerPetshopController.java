package br.com.vemser.petshop.controller;

import br.com.vemser.petshop.dto.balancomensal.BalancoMensalDTO;
import br.com.vemser.petshop.dto.pedidomensal.PedidoMensalDto;
import br.com.vemser.petshop.service.ConsumerPetshopService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/balanco-mensal")
@RequiredArgsConstructor
public class ConsumerPetshopController {
    private final ConsumerPetshopService consumerPetshopService;

    @Operation(summary = "Recuperar balanço mensal atual", description = "Retorna os dados do balanço " +
            "mensal do mês atual")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retorna o balanço"),
                    @ApiResponse(responseCode = "400", description = "Erro client-side"),
                    @ApiResponse(responseCode = "404", description = "Balanço mensal não encontrado"),
                    @ApiResponse(responseCode = "500", description = "Erro server-side")
            }
    )
    @GetMapping("/mes-atual")
    public ResponseEntity<BalancoMensalDTO> getMesAtual() {
        return new ResponseEntity<>(consumerPetshopService.getBalancoMesAtual(), HttpStatus.OK);
    }

    @Operation(summary = "Recuperar balanço mensal", description = "Retorna os dados do balanço " +
            "mensal a partir de um mês e ano informado")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retorna o balanço"),
                    @ApiResponse(responseCode = "400", description = "Erro client-side"),
                    @ApiResponse(responseCode = "404", description = "Balanço mensal não encontrado"),
                    @ApiResponse(responseCode = "500", description = "Erro server-side")
            }
    )
    @GetMapping("/mes/{mes}/ano/{ano}")
    public ResponseEntity<BalancoMensalDTO> getByMesAno(@PathVariable("mes") Integer mes, @PathVariable("ano") Integer ano) {
        return new ResponseEntity<>(consumerPetshopService.getBalancoByMesAndAno(mes, ano), HttpStatus.OK);
    }

    @Operation(summary = "Recuperar pedidos do mês atual", description = "Retorna os pedidos gerados no mês atual")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retorna os pedidos"),
                    @ApiResponse(responseCode = "400", description = "Erro client-side"),
                    @ApiResponse(responseCode = "404", description = "Registro de pedidos não encontrado"),
                    @ApiResponse(responseCode = "500", description = "Erro server-side")
            }
    )
    @GetMapping("/pedidos-mes-atual")
    public ResponseEntity<PedidoMensalDto> getPedidosMesAtual() {
        return ResponseEntity.ok(consumerPetshopService.getPedidoMesAtual());
    }

    @Operation(summary = "Recuperar pedidos a partir do mês e ano", description = "Retorna os pedidos gerados no mês e ano informados")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retorna os pedidos"),
                    @ApiResponse(responseCode = "400", description = "Erro client-side"),
                    @ApiResponse(responseCode = "404", description = "Registro de pedidos não encontrado"),
                    @ApiResponse(responseCode = "500", description = "Erro server-side")
            }
    )
    @GetMapping("/pedidos/mes/{mes}/ano/{ano}")
    public ResponseEntity<PedidoMensalDto> getPedidosByMesAno(@PathVariable("mes") Integer mes, @PathVariable("ano") Integer ano) {
        return ResponseEntity.ok(consumerPetshopService.getPedidoByMesAndAno(mes, ano));
    }

}
